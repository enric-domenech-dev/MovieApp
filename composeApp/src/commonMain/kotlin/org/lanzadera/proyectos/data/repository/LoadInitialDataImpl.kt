package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.utils.Logger
import org.lanzadera.proyectos.utils.Constants
import org.lanzadera.proyectos.domain.repository.LoadInitialData
import org.lanzadera.proyectos.data.dto.movie.MovieResponseDto
import org.lanzadera.proyectos.data.mapper.toDomain

class LoadInitialDataImpl(
    private val client: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : LoadInitialData {

    // Core
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _movies

    private val _trending = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesFlow: StateFlow<List<Movie>> = _trending

    // Additional
    private val _popular = MutableStateFlow<List<Movie>>(emptyList())
    override val popularMoviesFlow: StateFlow<List<Movie>> = _popular

    private val _topRated = MutableStateFlow<List<Movie>>(emptyList())
    override val topRatedMoviesFlow: StateFlow<List<Movie>> = _topRated

    private val _upcoming = MutableStateFlow<List<Movie>>(emptyList())
    override val upcomingMoviesFlow: StateFlow<List<Movie>> = _upcoming

    private val _discover = MutableStateFlow<List<Movie>>(emptyList())
    override val discoverMoviesFlow: StateFlow<List<Movie>> = _discover

    private val _hero = MutableStateFlow<List<Movie>>(emptyList())
    override val heroMoviesFlow: StateFlow<List<Movie>> = _hero

    private val _trendingDaily = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesDailyFlow: StateFlow<List<Movie>> = _trendingDaily

    private val _inCinemasToday = MutableStateFlow<List<Movie>>(emptyList())
    override val inCinemasTodayFlow: StateFlow<List<Movie>> = _inCinemasToday

    // (Opcional) TTL por feed para evitar sobrecarga
    private val lastUpdated = mutableMapOf<MutableStateFlow<List<Movie>>, Long>()
    private val ttl = Constants.Cache.DEFAULT_TTL_MS

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


// --- Utilidad de refresco con TTL ---

private suspend inline fun refreshFeed(
    state: MutableStateFlow<List<Movie>>,
    force: Boolean,
    ttlMillis: Long = 0L,                      // 0 = sin TTL
    lastUpdated: MutableMap<MutableStateFlow<List<Movie>>, Long>,
    crossinline fetch: suspend () -> List<Movie>
) {
    val now = Clock.System.now().toEpochMilliseconds()
    val last = lastUpdated[state] ?: 0L
    val freshEnough = ttlMillis > 0 && (now - last) < ttlMillis

    Logger.d("force=$force, state.size=${state.value.size}, freshEnough=$freshEnough, ttlMillis=$ttlMillis, last=$last, now=$now", tag = "LoadInitialData")
    if (!force && (state.value.isNotEmpty() || freshEnough)) {
        Logger.d("skipping fetch, cache is fresh or not forced", tag = "LoadInitialData")
        return
    }
    Logger.d("fetching new data", tag = "LoadInitialData")
    val data = fetch()
    state.value = data                          // emitir ANTES de devolver
    lastUpdated[state] = now
    Logger.d("updated state with ${data.size} movies", tag = "LoadInitialData")
}


    // --- Core refresh ---
    override suspend fun refreshMovies(force: Boolean) =
        refreshFeed(_movies, force, ttl, lastUpdated) { fetchNowPlayingMovies() }

    override suspend fun refreshTrendingMovies(force: Boolean) =
        refreshFeed(_trending, force, ttl, lastUpdated) { fetchTrendingMoviesWeek() }

    // --- Additional refresh ---
    override suspend fun refreshPopularMovies(force: Boolean) =
        refreshFeed(_popular, force, ttl, lastUpdated) { fetchPopularMovies() }

    override suspend fun refreshTopRatedMovies(force: Boolean) =
        refreshFeed(_topRated, force, ttl, lastUpdated) { fetchTopRatedMovies() }

    override suspend fun refreshUpcomingMovies(force: Boolean) =
        refreshFeed(_upcoming, force, ttl, lastUpdated) { fetchUpcomingMovies() }

    override suspend fun refreshDiscoverMovies(force: Boolean) {
        refreshFeed(_discover, force, ttl, lastUpdated) { fetchTrendingMovies() }
    }

    override suspend fun refreshHeroMovies(force: Boolean) =
        refreshFeed(_hero, force, ttl, lastUpdated) { fetchHeroMovies() }

    override suspend fun refreshTrendingMoviesDaily(force: Boolean) =
        refreshFeed(_trendingDaily, force, ttl, lastUpdated) { fetchTrendingMoviesDay() }

    override suspend fun refreshInCinemasToday(force: Boolean) {
        refreshFeed(_inCinemasToday, force, ttl, lastUpdated) { fetchInCinemasToday() }
    }


// --- Fetchers concretos (reutiliza los tuyos) ---

    private suspend fun fetchTrendingMoviesWeek(): List<Movie> =
        fetchPaged("/3/trending/movie/week", mapOf("language" to "es"))

    private suspend fun fetchTrendingMoviesDay(): List<Movie> =
        fetchPaged("/3/trending/movie/day", mapOf("language" to "es"))

    private suspend fun fetchTrendingMovies(): List<Movie> =
        fetchPaged(
            path = "/3/trending/movie/week",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchHeroMovies(): List<Movie> {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        val from = today.minus(DatePeriod(days = 30)).toString()
        val to = today.plus(DatePeriod(days = 90)).toString()
        return fetchPaged(
            "/3/discover/movie",
            mapOf(
                "include_adult" to "false",
                "include_video" to "false",
                "sort_by" to "popularity.desc",
                "with_release_type" to "3|2",            // theatrical primero
                "release_date.gte" to from,              // ventana reciente/próxima
                "release_date.lte" to to,
                "vote_count.gte" to "200",
                "language" to "es"
            )
        )
    }

    private suspend fun fetchInCinemasToday(
        limit: Int = 30,
        lookBackDays: Int = 90,
    ): List<Movie> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val from = today.minus(DatePeriod(days = lookBackDays)).toString()
        val params = buildMap {
            put("include_adult", "false")
            put("sort_by", "popularity.desc")
            put("with_release_type", "3|2")               // cines
            put("release_date.gte", from)
            put("language", "es")
            put("release_date.lte", today.toString())
        }
        return fetchPaged("/3/discover/movie", params).distinctBy { it.id }.take(limit)
    }

    private suspend fun fetchPopularMovies(): List<Movie> =
        fetchPaged(
            path = "/3/movie/popular",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchTopRatedMovies(): List<Movie> =
        fetchPaged("/3/movie/top_rated", mapOf("language" to "es"))

    private suspend fun fetchUpcomingMovies(): List<Movie> =
        fetchPaged(
            path = "/3/movie/upcoming",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchNowPlayingMovies(): List<Movie> =
        fetchPaged("/3/movie/now_playing", mapOf("language" to "es"))

    // --- Paginación común ---
    private suspend fun fetchPaged(path: String, baseParams: Map<String, String>): List<Movie> {
        val acc = mutableListOf<Movie>()
        // Si tu MovieResponse tiene total_pages/page, puedes cortar antes.
        for (page in 1..maxPages) {
            val text = client.get(path) {
                url {
                    baseParams.forEach { (k, v) -> parameters.append(k, v) }
                    parameters.append("page", page.toString())
                }
            }.bodyAsText()

            val dto: MovieResponseDto = json.decodeFromString(text)
            val domainMovies = dto.results.map { it.toDomain() }
            val valid = domainMovies.filter(::isValidMovie)

            if (valid.isEmpty()) break
            acc += valid
            Logger.d("page $page, downloaded ${valid.size} movies, total so far: ${acc.size}", tag = "LoadInitialData")

        }
        Logger.d("finished, total movies downloaded: ${acc.size}", tag = "LoadInitialData")
        return acc
    }
}
