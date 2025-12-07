package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.movie.MovieResponseDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowResponseDto
import org.lanzadera.proyectos.data.mapper.toDomain
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.SearchRepository
import org.lanzadera.proyectos.utils.Logger

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

            val responseDto = json.decodeFromString<MovieResponseDto>(text)
            responseDto.toDomain().results
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error searching movies with query: $query", tag = "SearchRepository", throwable = e)
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

            val responseDto = json.decodeFromString<TvShowResponseDto>(text)
            responseDto.toDomain().results
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error searching TV shows with query: $query", tag = "SearchRepository", throwable = e)
            emptyList()
        }
    }
}

