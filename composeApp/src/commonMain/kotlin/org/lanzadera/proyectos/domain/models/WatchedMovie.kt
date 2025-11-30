package org.lanzadera.proyectos.domain.models

data class WatchedMovie(
    val movieId: String
) {
    val id: String get() = movieId
}
