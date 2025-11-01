package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.movie.MovieResponse
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.models.tvshow.TvShowResponse
import org.lanzadera.proyectos.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val client: HttpClient,
    private val json: Json
) : SearchRepository {
    override suspend fun searchMovies(
        query: String,
        page: Int,
        language: String
    ): List<Movie> {
        return try {
            val text = client.get("/3/search/movie") {
                url {
                    parameters.append("query", query)
                    parameters.append("include_adult", "false")
                    parameters.append("language", language)
                    parameters.append("page", page.toString())
                }
            }.bodyAsText()

            val response = json.decodeFromString<MovieResponse>(text)
            response.results
        } catch (e: Exception) {
            println("Error searching movies: ${e.message}")
            emptyList()
        }
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        language: String
    ): List<TvShow> {
        return try {
            val text = client.get("/3/search/tv") {
                url {
                    parameters.append("query", query)
                    parameters.append("include_adult", "false")
                    parameters.append("language", language)
                    parameters.append("page", page.toString())
                }
            }.bodyAsText()

            val response = json.decodeFromString<TvShowResponse>(text)
            response.results
        } catch (e: Exception) {
            println("Error searching TV shows: ${e.message}")
            emptyList()
        }
    }
}

