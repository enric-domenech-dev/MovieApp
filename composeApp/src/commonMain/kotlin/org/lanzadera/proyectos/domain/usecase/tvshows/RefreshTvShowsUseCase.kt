package org.lanzadera.proyectos.domain.usecase.tvshows

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.utils.Logger

class RefreshTvShowsUseCase(private val repository: TvShowRepository) {
    val tvShowsFlow: StateFlow<List<TvShow>> get() = repository.tvShowsFlow
    val popularTvShowsFlow: StateFlow<List<TvShow>> get() = repository.popularTvShowsFlow
    val topRatedTvShowsFlow: StateFlow<List<TvShow>> get() = repository.topRatedTvShowsFlow
    val onAirTvShowsFlow: StateFlow<List<TvShow>> get() = repository.onAirTvShowsFlow
    val trendingTvShowsFlow: StateFlow<List<TvShow>> get() = repository.trendingTvShowsFlow
    val airingTodayTvShowsFlow: StateFlow<List<TvShow>> get() = repository.airingTodayTvShowsFlow
    val trendingTvShowsWeekFlow: StateFlow<List<TvShow>> get() = repository.trendingTvShowsWeekFlow

    suspend fun refreshTvShows(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshTvShows(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing TV shows", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshPopularTvShows(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshPopularTvShows(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing popular TV shows", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshTopRatedTvShows(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshTopRatedTvShows(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing top rated TV shows", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshOnAirTvShows(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshOnAirTvShows(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing on air TV shows", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshTrendingTvShows(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshTrendingTvShows(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing trending TV shows", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshAiringTodayTvShows(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshAiringTodayTvShows(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing airing today TV shows", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshTrendingTvShowsWeek(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshTrendingTvShowsWeek(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing trending TV shows week", tag = "RefreshTvShowsUseCase", throwable = e)
            Result.Error(e)
        }
    }
}

