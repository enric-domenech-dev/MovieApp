package org.lanzadera.proyectos.domain.usecase.tvshows

import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.TvShowRepository

class GetTvShowDetailsUseCase(private val repository: TvShowRepository) {
    suspend fun execute(tvShowId: Int): TvShow? = repository.getTvShowDetails(tvShowId)
}

