package org.lanzadera.proyectos.ui.screens.home

import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.models.movie.MovieResponse

class HomeViewModel(
    private val client: HttpClient,
    private val maxPages: Int = 20
) : ViewModel() {

    suspend fun initUIState(): UIState {
        return try {
            coroutineScope {
                val trendingMoviesDeferred = async { fetchTrendingMovies() }
                val allMoviesDeferred = async { fetchAllMovies() }

                val trendingMovies = trendingMoviesDeferred.await()
                val allMovies = allMoviesDeferred.await()

                UIState.Success(
                    movies = allMovies,
                    trendingMovies = trendingMovies
                )
            }
        } catch (e: Exception) {
            UIState.Error("Error al cargar las películas: ${e.message}")
        }
    }

    private suspend fun fetchTrendingMovies(): List<Movie> {
        val allMovies = mutableListOf<Movie>()
        var currentPage = 1

        do {
            val response: HttpResponse = client.get("/3/trending/movie/week") {
                url {
                    parameters.append("language", "es")
                    parameters.append("page", currentPage.toString())
                }
            }

            val movieResponse: MovieResponse = Json.decodeFromString(response.bodyAsText())

            val validMovies = movieResponse.results.filterNot { movie ->
                movie.id == null || movie.title == null || movie.posterPath == null ||
                        movie.overview == null || movie.releaseDate == null || movie.voteCount == null ||
                        movie.popularity == null || movie.originalLanguage == null || movie.originalTitle == null ||
                        movie.backdropPath == null || movie.adult == null || movie.video == null
            }

            allMovies.addAll(validMovies)
            currentPage++
        } while (currentPage <= maxPages/maxPages)

        return allMovies
    }

    private suspend fun fetchAllMovies(): List<Movie> {
        val allMovies = mutableListOf<Movie>()
        var currentPage = 1

        do {
            val response: HttpResponse = client.get("/3/discover/movie") {
                url {
                    parameters.append("language", "es")
                    parameters.append("sort_by", "popularity.desc")
                    parameters.append("page", currentPage.toString())
                }
            }

            val movieResponse: MovieResponse = Json.decodeFromString(response.bodyAsText())

            val validMovies = movieResponse.results.filterNot { movie ->
                movie.id == null || movie.title == null || movie.posterPath == null ||
                        movie.overview == null || movie.releaseDate == null || movie.voteCount == null ||
                        movie.popularity == null || movie.originalLanguage == null || movie.originalTitle == null ||
                        movie.backdropPath == null || movie.adult == null || movie.video == null
            }

            allMovies.addAll(validMovies)
            currentPage++
        } while (currentPage <= maxPages)

        return allMovies
    }

    open class UIState {
        object Loading : UIState()
        data class Success(
            val movies: List<Movie>,
            val trendingMovies: List<Movie>
        ) : UIState()

        data class Error(val message: String) : UIState()
    }
}
