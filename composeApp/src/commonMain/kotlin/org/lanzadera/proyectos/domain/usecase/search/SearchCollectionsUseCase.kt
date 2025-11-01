package org.lanzadera.proyectos.domain.usecase.search

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.SearchRepository

class SearchMoviesUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): List<Movie> {
        if (query.trim().isEmpty()) {
            return emptyList()
        }
        return searchRepository.searchMovies(query, page, language)
    }
}

