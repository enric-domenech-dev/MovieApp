package org.lanzadera.proyectos.domain.usecase.tvshows

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.utils.Logger

class GetTvShowDetailsUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Int): Result<TvShow?> {
        return try {
            val tvShow = repository.getTvShowDetails(tvShowId)
            Logger.d("Fetched details for TV show: $tvShowId", tag = "GetTvShowDetailsUseCase")
            Result.Success(tvShow)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error fetching TV show details for: $tvShowId", tag = "GetTvShowDetailsUseCase", throwable = e)
            Result.Error(e, "No se pudieron cargar los detalles de la serie")
        }
    }
}

