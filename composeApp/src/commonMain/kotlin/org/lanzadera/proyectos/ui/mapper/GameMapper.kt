package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.ui.models.GameUI

/**
 * Maps domain Game to UI GameUI.
 */
fun Game.toUI(): GameUI = GameUI(
    id = id ?: 0,
    name = name ?: "",
    summary = summary,
    coverUrl = cover?.url,
    releaseDate = firstReleaseDate,
    rating = rating,
    ratingCount = ratingCount,
    genres = genres?.mapNotNull { it.name },
    platforms = platforms?.mapNotNull { it.name }
)

/**
 * Maps list of domain Games to UI GameUIs.
 */
fun List<Game>.toUI(): List<GameUI> = map { it.toUI() }
