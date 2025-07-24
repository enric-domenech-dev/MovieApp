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

class HomeViewModel : ViewModel() {

    private val client = HttpClient()

    suspend fun initUIState(): UIState {
        return try {
            // Usar coroutineScope para crear un scope para las corrutinas paralelas
            coroutineScope {
                // Ejecutar ambas llamadas en paralelo para mejor rendimiento
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
            val response: HttpResponse = client.get("https://api.themoviedb.org/3/trending/movie/week") {
                headers {
                    append("accept", "application/json")
                    append(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
                    )
                }
                url {
                    parameters.append("language", "es")
                    parameters.append("page", currentPage.toString())
                }
            }

            val movieResponse: MovieResponse = Json.decodeFromString(response.bodyAsText())

            // Filtrar resultados para manejar valores nulos
            val validMovies = movieResponse.results.filterNot { movie ->
                movie.id == null || movie.title == null || movie.posterPath == null ||
                        movie.overview == null || movie.releaseDate == null || movie.voteAverage == null ||
                        movie.voteCount == null || movie.popularity == null || movie.originalLanguage == null ||
                        movie.originalTitle == null || movie.backdropPath == null || movie.adult == null ||
                        movie.video == null
            }

            allMovies.addAll(validMovies)
            currentPage++
        } while (currentPage <= 10)

        return allMovies
    }

    private suspend fun fetchAllMovies(): List<Movie> {
        val allMovies = mutableListOf<Movie>()
        var currentPage = 1

        do {
            val response: HttpResponse = client.get("https://api.themoviedb.org/3/discover/movie") {
                headers {
                    append("accept", "application/json")
                    append(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
                    )
                }
                url {
                    parameters.append("language", "es")
                    parameters.append("sort_by", "popularity.desc")
                    parameters.append("page", currentPage.toString())
                }
            }

            val movieResponse: MovieResponse = Json.decodeFromString(response.bodyAsText())

            // Filtrar resultados para manejar valores nulos
            val validMovies = movieResponse.results.filterNot { movie ->
                movie.id == null || movie.title == null || movie.posterPath == null ||
                        movie.overview == null || movie.releaseDate == null || movie.voteAverage == null ||
                        movie.voteCount == null || movie.popularity == null || movie.originalLanguage == null ||
                        movie.originalTitle == null || movie.backdropPath == null || movie.adult == null ||
                        movie.video == null
            }

            allMovies.addAll(validMovies)
            currentPage++
        } while (currentPage <= 10)

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