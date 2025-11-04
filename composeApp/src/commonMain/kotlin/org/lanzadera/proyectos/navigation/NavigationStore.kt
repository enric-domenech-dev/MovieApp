package org.lanzadera.proyectos.navigation

import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

/**
 * Temporary in-memory store to pass complex objects between destinations when
 * serializing in the nav route is not desired. It's a simple compromise for this
 * project; for production prefer using IDs and fetching details in the target.
 */
object NavigationStore {
    var selectedMovie: Movie? = null
    var selectedBook: Book? = null
    var selectedTvShow: TvShow? = null
    var selectedGame: Game? = null
}
