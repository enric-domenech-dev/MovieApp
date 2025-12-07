package org.lanzadera.proyectos.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using Navigation 3 with @Serializable.
 * 
 * Each screen is a sealed interface member with its required parameters.
 * Navigation 3 handles serialization/deserialization automatically.
 * 
 * Usage:
 * ```kotlin
 * // Navigate
 * navController.navigate(Screen.MovieDetail(movieId = 123))
 * 
 * // In composable
 * composable<Screen.MovieDetail> { backStackEntry ->
 *     val args = backStackEntry.toRoute<Screen.MovieDetail>()
 *     MovieDetailView(movieId = args.movieId)
 * }
 * ```
 * 
 * @see https://developer.android.com/guide/navigation/design/type-safety
 */
sealed interface Screen {
    
    @Serializable
    data object SplashScreen : Screen
    
    @Serializable
    data object Home : Screen
    
    @Serializable
    data object Login : Screen
    
    @Serializable
    data object Search : Screen
    
    @Serializable
    data object Chat : Screen
    
    @Serializable
    data object Profile : Screen
    
    @Serializable
    data object Settings : Screen
    
    // Detail screens with IDs
    
    @Serializable
    data class MovieDetail(val movieId: Int) : Screen
    
    @Serializable
    data class TvShowDetail(val tvShowId: Int) : Screen
    
    @Serializable
    data class GameDetail(val gameId: Int) : Screen
    
    @Serializable
    data class BookDetail(val bookId: String) : Screen
}
