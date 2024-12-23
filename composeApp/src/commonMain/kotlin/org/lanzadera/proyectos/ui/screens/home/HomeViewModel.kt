package org.lanzadera.proyectos.ui.screens.home

import io.ktor.client.request.*
import io.ktor.client.statement.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import movieapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.models.movie.MovieResponse
import org.lanzadera.proyectos.utils.Constants


class HomeViewModel : ViewModel() {

    private val client = HttpClient()
    suspend fun data(): List<Movie> {
        println(Constants.API_KEY)
        val response: HttpResponse =
            client.get("https://api.themoviedb.org/3/discover/movie?language=en-US&sort_by=popularity.desc") {
                headers {
                    append("accept", "application/json")
                    append(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
                    )
                }
            }
        println(response.bodyAsText())
        return Json.decodeFromString<MovieResponse>(response.body()).results
    }


    suspend fun fetchAllMovies(): List<Movie> {
        val allMovies = mutableListOf<Movie>()
        var currentPage = 1
        var totalPages: Int

        do {
            val response: HttpResponse = client.get("https://api.themoviedb.org/3/discover/movie") {
                url {
                    parameters.append("language", "en-US")
                    parameters.append("sort_by", "popularity.desc")
                    parameters.append("page", currentPage.toString())
                }
                headers {
                    append("accept", "application/json")
                    append(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
                    )
                }
            }

            val movieResponse: MovieResponse = Json.decodeFromString(response.bodyAsText())
            totalPages = movieResponse.totalPages
            // Filtrar resultados para manejar valores nulos
            val validMovies = movieResponse.results.filterNot { movie ->
                println(movie)
                movie.id == null || movie.title == null || movie.posterPath == null || movie.overview == null || movie.releaseDate == null || movie.voteAverage == null || movie.voteCount == null || movie.popularity == null || movie.originalLanguage == null || movie.originalTitle == null || movie.backdropPath == null || movie.adult == null || movie.video == null
            }

            allMovies.addAll(validMovies)
            currentPage++
        } while (currentPage <= 10)

        return allMovies
    }

}