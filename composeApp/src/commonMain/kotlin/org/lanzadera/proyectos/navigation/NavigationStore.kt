package org.lanzadera.proyectos.navigation

import org.lanzadera.proyectos.domain.models.movie.Movie

/**
 * Temporary in-memory store to pass complex objects between destinations when
 * serializing in the nav route is not desired. It's a simple compromise for this
 * project; for production prefer using IDs and fetching details in the target.
 */
object NavigationStore {
    var selectedMovie: Movie? = null
}

