package org.lanzadera.proyectos.ui.models

/**
 * Value Object que determina a qué categoría de filtro pertenece un item favorito.
 */
sealed class FavoriteFilterCategory {
    object Available : FavoriteFilterCategory()
    object Upcoming : FavoriteFilterCategory()
    object InProduction : FavoriteFilterCategory()
    object Ended : FavoriteFilterCategory()

    companion object {
        /**
         * Determina la categoría de filtro para una serie basándose en su disponibilidad y status.
         */
        fun fromSeriesItem(
            isAvailable: Boolean,
            daysUntilAvailable: Int?,
            status: String?
        ): FavoriteFilterCategory {
            // Prioridad: Disponibilidad > Status
            return when {
                isAvailable -> Available // Episodio disponible AHORA
                daysUntilAvailable != null -> Upcoming // Episodio próximamente
                else -> fromStatus(status) // Sin episodios con fecha, verificar status
            }
        }

        /**
         * Determina la categoría basándose solo en el status de TMDB.
         */
        fun fromStatus(status: String?): FavoriteFilterCategory {
            return when (status?.lowercase()) {
                "ended", "canceled", "cancelled" -> Ended
                "returning series", "in production", "planned" -> InProduction
                else -> Available
            }
        }
    }
}

/**
 * Extension function para determinar si un item debe mostrarse según los filtros activos.
 */
fun FavoriteItemWithInfoUI.shouldShowWithFilters(
    showAvailableSeries: Boolean,
    showUpcomingSeries: Boolean,
    showInProductionSeries: Boolean,
    showEndedSeries: Boolean,
    showAvailableMovies: Boolean,
    showUpcomingMovies: Boolean
): Boolean {
    return when (this) {
        is FavoriteItemWithInfoUI.TvShowItem -> {
            val category = FavoriteFilterCategory.fromSeriesItem(
                isAvailable = isAvailable,
                daysUntilAvailable = daysUntilAvailable,
                status = tvShowWithNext.tvShow.status
            )

            when (category) {
                FavoriteFilterCategory.Available -> showAvailableSeries
                FavoriteFilterCategory.Upcoming -> showUpcomingSeries
                FavoriteFilterCategory.InProduction -> showInProductionSeries
                FavoriteFilterCategory.Ended -> showEndedSeries
            }
        }

        is FavoriteItemWithInfoUI.InProductionSeriesItem -> {
            val category = FavoriteFilterCategory.fromStatus(tvShow.status)
            when (category) {
                FavoriteFilterCategory.Available -> showAvailableSeries
                FavoriteFilterCategory.Upcoming -> showUpcomingSeries
                FavoriteFilterCategory.InProduction -> showInProductionSeries
                FavoriteFilterCategory.Ended -> showEndedSeries
            }
        }

        is FavoriteItemWithInfoUI.FinishedSeriesItem -> {
            val category = FavoriteFilterCategory.fromStatus(tvShow.status)
            when (category) {
                FavoriteFilterCategory.Available -> showAvailableSeries
                FavoriteFilterCategory.Upcoming -> showUpcomingSeries
                FavoriteFilterCategory.InProduction -> showInProductionSeries
                FavoriteFilterCategory.Ended -> showEndedSeries
            }
        }

        is FavoriteItemWithInfoUI.MovieItem -> {
            if (isAvailable) showAvailableMovies else showUpcomingMovies
        }

        is FavoriteItemWithInfoUI.WatchedMovieItem -> showAvailableMovies
    }
}
