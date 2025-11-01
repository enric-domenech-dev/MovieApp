package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.movie.MovieResponse
import org.lanzadera.proyectos.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val client: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : MovieRepository {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _movies

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    override val popularMoviesFlow: StateFlow<List<Movie>> = _popularMovies

    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList())
    override val topRatedMoviesFlow: StateFlow<List<Movie>> = _topRatedMovies

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    override val upcomingMoviesFlow: StateFlow<List<Movie>> = _upcomingMovies

    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesFlow: StateFlow<List<Movie>> = _trendingMovies

    private val lastUpdated = mutableMapOf<MutableStateFlow<List<Movie>>, Long>()
    private val TTL = 2 * 60 * 1000L // 2 min

    private val movieDetailsCache = mutableMapOf<Int, Movie>()

    private fun isValidMovie(movie: Movie): Boolean =
        movie.id != null &&
                movie.title != null &&
                movie.posterPath != null &&
                movie.overview != null &&
                movie.releaseDate != null &&
                movie.voteCount != null &&
                movie.popularity != null &&
                movie.originalLanguage != null &&
                movie.backdropPath != null

    private suspend inline fun refreshFeed(
        state: MutableStateFlow<List<Movie>>,
        force: Boolean,
        ttlMillis: Long = 0L,
        lastUpdated: MutableMap<MutableStateFlow<List<Movie>>, Long>,
        crossinline fetch: suspend () -> List<Movie>
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        val last = lastUpdated[state] ?: 0L
        val freshEnough = ttlMillis > 0 && (now - last) < ttlMillis

        println("SYNCRO MovieRepositoryImpl: force=$force, state.size=${state.value.size}, freshEnough=$freshEnough")
        if (!force && (state.value.isNotEmpty() || freshEnough)) {
            println("SYNCRO MovieRepositoryImpl: skipping fetch, cache is fresh")
            return
        }
        println("SYNCRO MovieRepositoryImpl: fetching new data")
        val data = fetch()
        state.value = data
        lastUpdated[state] = now
        println("SYNCRO MovieRepositoryImpl: updated state with ${data.size} movies")
    }

    override suspend fun refreshMovies(force: Boolean) =
        refreshFeed(_movies, force, TTL, lastUpdated) { fetchTrendingMoviesWeek() }

    override suspend fun refreshPopularMovies(force: Boolean) =
        refreshFeed(_popularMovies, force, TTL, lastUpdated) { fetchPopularMovies() }

    override suspend fun refreshTopRatedMovies(force: Boolean) =
        refreshFeed(_topRatedMovies, force, TTL, lastUpdated) { fetchTopRatedMovies() }

    override suspend fun refreshUpcomingMovies(force: Boolean) =
        refreshFeed(_upcomingMovies, force, TTL, lastUpdated) { fetchUpcomingMovies() }

    override suspend fun refreshTrendingMovies(force: Boolean) =
        refreshFeed(_trendingMovies, force, TTL, lastUpdated) { fetchTrendingMoviesDay() }

    override suspend fun getMovieDetails(movieId: Int): Movie? {
        // Intenta cache primero
        if (movieDetailsCache.containsKey(movieId)) {
            return movieDetailsCache[movieId]
        }

        return try {
            val text = client.get("/3/movie/$movieId") {
                url {
                    parameters.append("append_to_response", "aggregate_credits")
                    parameters.append("language", "es")
                }
            }.bodyAsText()
            val movie: Movie = json.decodeFromString(text)
            movieDetailsCache[movieId] = movie
            movie
        } catch (t: Throwable) {
            println("SYNCRO MovieRepositoryImpl: error fetching movie details: ${t.message}")
            null
        }
    }

    private suspend fun fetchTrendingMoviesWeek(): List<Movie> =
        fetchPaged("/3/trending/movie/week", mapOf("language" to "es"))

    private suspend fun fetchTrendingMoviesDay(): List<Movie> =
        fetchPaged("/3/trending/movie/day", mapOf("language" to "es"))

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

    private suspend fun fetchPaged(path: String, baseParams: Map<String, String>): List<Movie> {
        val acc = mutableListOf<Movie>()
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
            println("SYNCRO fetchPaged Movie: page $page, downloaded ${valid.size} movies, total so far: ${acc.size}")
        }
        println("SYNCRO fetchPaged Movie: finished, total movies downloaded: ${acc.size}")
        return acc
    }
}

