package org.lanzadera.proyectos.domain.models.favorite

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Compact representation for anything the user can mark as favorite.
 */
data class FavoriteItem(
    val id: String,
    val type: FavoriteType,
    val title: String,
    val posterUrl: String? = null,
    val overview: String? = null,
    val addedAt: Instant = Clock.System.now(),
    val updatedAt: Instant = addedAt
)

@Suppress("MagicNumber")
enum class FavoriteType {
    MOVIE,
    TV_SHOW,
    BOOK,
    GAME
}

