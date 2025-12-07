package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.movie.MovieDto
import org.lanzadera.proyectos.data.dto.movie.MovieResponseDto
import org.lanzadera.proyectos.data.mapper.toDomain
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.utils.Constants
import org.lanzadera.proyectos.utils.Logger

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
    private val ttl = Constants.Cache.DEFAULT_TTL_MS

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
        try {
            val now = Clock.System.now().toEpochMilliseconds()
            val last = lastUpdated[state] ?: 0L
            val freshEnough = ttlMillis > 0 && (now - last) < ttlMillis

            Logger.d("force=$force, state.size=${state.value.size}, freshEnough=$freshEnough", tag = "MovieRepository")
            if (!force && (state.value.isNotEmpty() || freshEnough)) {
                Logger.d("skipping fetch, cache is fresh", tag = "MovieRepository")
                return
            }
            Logger.d("fetching new data", tag = "MovieRepository")
            val data = fetch()
            state.value = data
            lastUpdated[state] = now
            Logger.d("updated state with ${data.size} movies", tag = "MovieRepository")
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing movie feed", tag = "MovieRepository", throwable = e)
        }
    }

    override suspend fun refreshMovies(force: Boolean) =
        refreshFeed(_movies, force, ttl, lastUpdated) { fetchTrendingMoviesWeek() }

    override suspend fun refreshPopularMovies(force: Boolean) =
        refreshFeed(_popularMovies, force, ttl, lastUpdated) { fetchPopularMovies() }

    override suspend fun refreshTopRatedMovies(force: Boolean) =
        refreshFeed(_topRatedMovies, force, ttl, lastUpdated) { fetchTopRatedMovies() }

    override suspend fun refreshUpcomingMovies(force: Boolean) =
        refreshFeed(_upcomingMovies, force, ttl, lastUpdated) { fetchUpcomingMovies() }

    override suspend fun refreshTrendingMovies(force: Boolean) =
        refreshFeed(_trendingMovies, force, ttl, lastUpdated) { fetchTrendingMoviesDay() }

    override suspend fun getMovieDetails(movieId: Int): Movie? {
        // Try cache first
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
            val dto: MovieDto = json.decodeFromString(text)
            val movie = dto.toDomain()
            movieDetailsCache[movieId] = movie
            movie
        } catch (t: Throwable) {
            Logger.e("error fetching movie details: ${t.message}", tag = "MovieRepository", throwable = t)
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
        return try {
            val acc = mutableListOf<Movie>()
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
                Logger.d("page $page, downloaded ${valid.size} movies, total so far: ${acc.size}", tag = "MovieRepository")
            }
            Logger.d("finished, total movies downloaded: ${acc.size}", tag = "MovieRepository")
            acc
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error fetching paged movies from $path", tag = "MovieRepository", throwable = e)
            emptyList()
        }
    }
}

