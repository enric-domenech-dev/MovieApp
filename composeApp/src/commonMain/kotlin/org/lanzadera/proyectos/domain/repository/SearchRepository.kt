package org.lanzadera.proyectos.domain.repository

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

/**
 * Repository for searching movies and TV shows.
 *
 * Provides search functionality against TMDB API.
 */
interface SearchRepository {
    /**
     * Searches for movies matching the query.
     * @param query Search query string
     * @param page Page number for pagination
     * @param language Language code (default: en-US)
     * @return List of matching movies
     */
    suspend fun searchMovies(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): List<Movie>

    /**
     * Searches for TV shows matching the query.
     * @param query Search query string
     * @param page Page number for pagination
     * @param language Language code (default: en-US)
     * @return List of matching TV shows
     */
    suspend fun searchTvShows(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): List<TvShow>
}

