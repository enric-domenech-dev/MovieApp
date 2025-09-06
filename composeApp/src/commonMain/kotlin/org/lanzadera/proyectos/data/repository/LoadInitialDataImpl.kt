package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.movie.MovieResponse
import org.lanzadera.proyectos.domain.repository.LoadInitialData

class LoadInitialDataImpl(
    private val client: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : LoadInitialData {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _movies

    private val _trending = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesFlow: StateFlow<List<Movie>> = _trending

    // Para evitar solapes de recargas
    private val moviesMutex = Mutex()
    private val trendingMutex = Mutex()

    override suspend fun refreshMovies(force: Boolean) {
        if (!force && _movies.value.isNotEmpty()) return
        moviesMutex.withLock {
            if (!force && _movies.value.isNotEmpty()) return
            _movies.value = fetchNowPlayingMovies()
        }
    }

    override suspend fun refreshTrendingMovies(force: Boolean) {
        if (!force && _trending.value.isNotEmpty()) return
        trendingMutex.withLock {
            if (!force && _trending.value.isNotEmpty()) return
            _trending.value = fetchInCinemasToday()
        }
    }

    // -------- privados --------

    private suspend fun fetchTrendingMovies(): List<Movie> =
        fetchPaged(
            path = "/3/trending/movie/week",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchHeroMovies(): List<Movie> {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        val from = today.minus(DatePeriod(days = 30)).toString() // YYYY-MM-DD
        val to = today.plus(DatePeriod(days = 30)).toString()

        return fetchPaged(
            path = "/3/discover/movie",
            baseParams = mapOf(
                "include_adult" to "false",
                "include_video" to "false",
                "sort_by" to "popularity.desc",
                "with_release_type" to "3|2",            // theatrical primero
                "release_date.gte" to from,              // ventana reciente/próxima
                "release_date.lte" to to,
                "vote_count.gte" to "200"
            )
        )
    }

    private suspend fun fetchInCinemasToday(
        limit: Int = 30,
        lookbackDays: Int = 90,
        region: String? = null // opcional: "ES", "US", etc.
    ): List<Movie> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val from = today.minus(DatePeriod(days = lookbackDays)).toString()

        val params = buildMap {
            put("include_adult", "false")
//            put("include_video", "false")
            put("sort_by", "popularity.desc")
            put("language", "es")              // mínimo 50 votos
//            put("with_release_type", "3|2")          // cines
            put("release_date.gte", from)            // estrenadas recientemente
            put("release_date.lte", today.toString())// …hasta hoy
            region?.let { put("region", it) }        // si quieres fecha regional
        }

        return fetchPaged(
            path = "/3/discover/movie",
            baseParams = params
        )
            .distinctBy { it.id }
            .take(limit)
    }

    private suspend fun fetchTrendingMoviesDaily(): List<Movie> =
        fetchPaged(
            path = "/3/trending/movie/day",
            baseParams = mapOf("language" to "en-US")
        )

    private suspend fun fetchDiscoverMovies(): List<Movie> =
        fetchPaged(
            path = "/3/discover/movie",
            baseParams = mapOf("language" to "es", "sort_by" to "popularity.desc")
        )

    private suspend fun fetchNowPlayingMovies(): List<Movie> =
        fetchPaged(
            path = "/3/movie/now_playing",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchPopularMovies(): List<Movie> =
        fetchPaged(
            path = "/3/movie/popular",
            baseParams = mapOf("language" to "en-US")
        )

    private suspend fun fetchTopRatedMovies(): List<Movie> =
        fetchPaged(
            path = "/3/movie/top_rated",
            baseParams = mapOf("language" to "en-US")
        )

    private suspend fun fetchUpcomingMovies(): List<Movie> =
        fetchPaged(
            path = "/3/movie/upcoming",
            baseParams = mapOf("language" to "en-US")
        )

    // Lógica común para paginación

    private suspend fun fetchPaged(
        path: String,
        baseParams: Map<String, String>
    ): List<Movie> {
        val acc = mutableListOf<Movie>()
        // Si tu MovieResponse tiene total_pages/page, puedes cortar antes.
        for (page in 1..maxPages) {
            val text = client.get(path) {
                url {
                    baseParams.forEach { (k, v) -> parameters.append(k, v) }
                    parameters.append("page", page.toString())
                }
            }.bodyAsText()

            val dto: MovieResponse = json.decodeFromString(text)
            val valid = dto.results.filter(::isValidMovie)

            if (valid.isEmpty()) break
            acc += valid
        }
        return acc
    }

    private fun isValidMovie(m: Movie): Boolean =
        m.id != null &&
                m.title != null &&
                m.posterPath != null &&
                m.overview != null &&
                m.releaseDate != null &&
                m.voteCount != null &&
                m.popularity != null &&
                m.originalLanguage != null &&
                m.originalTitle != null &&
                m.backdropPath != null &&
                m.adult != null &&
                m.video != null
}
