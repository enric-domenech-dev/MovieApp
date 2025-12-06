package org.lanzadera.proyectos.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.unit.dp

object Constants {

    // UI DIMENSIONS
    object Dimensions {
        val BOTTOM_NAV_BAR_HEIGHT = 80.dp  // Material 3 NavigationBar standard height
    }

    // SCREENS
    sealed class Screen(val route: String) {
        object Home : Screen("home")
        object Login : Screen("login")
        object SignIn : Screen("signIn")
        object SplashScreen : Screen("splashScreen")
        object Detail : Screen("detail")
        object MovieDetail : Screen("movieDetail/{movieId}") {
            fun createRoute(movieId: Int) = "movieDetail/$movieId"
        }

        object SeriesDetail : Screen("seriesDetail/{tvShowId}") {
            fun createRoute(tvShowId: Int) = "seriesDetail/$tvShowId"
        }
        object Search : Screen("search")
        object GameDetail : Screen("gameDetail/{gameId}") {
            fun createRoute(gameId: Int) = "gameDetail/$gameId"
        }
        // Added settings/chat/profile to centralize routes and avoid literals
        object Settings : Screen("settings")
        object Chat : Screen("chat")
        object Profile : Screen("profile")
    }

    // MENU OPTIONS
    object MenuOptions {
        val topBarTitles = listOf("SIGUIENDO", "LIBROS", "PELÍCULAS", "SERIES", "JUEGOS")
        val bottomBarTitles = listOf("Menu", "Buscar", "Inicio", "Chat", "Perfil")
        val bottomBarIcons = listOf(
            Icons.AutoMirrored.Outlined.List,
            Icons.Outlined.Search,
            Icons.Outlined.Home,
            Icons.Outlined.MailOutline,
            Icons.Outlined.Person
        )
        val bottomBarSelectedIcons = listOf(
            Icons.AutoMirrored.Filled.List,
            Icons.Filled.Search,
            Icons.Filled.Home,
            Icons.Filled.MailOutline,
            Icons.Filled.Person
        )
    }

    // NAVIGATION
    sealed class NavigationEvent {
        object NavigateToHome : NavigationEvent()
        object NavigateToLogin : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
        object NavigateBack : NavigationEvent()
        object NavigateToSplashScreen : NavigationEvent()
        object NavigateToDetail : NavigationEvent()
        object NavigateToSearch : NavigationEvent()
        object NavigateToSettings : NavigationEvent()
        object NavigateToChat : NavigationEvent()
        object NavigateToProfile : NavigationEvent()
    }

    //APY KEYS
    val API_KEY =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
    val BASE_URL = "https://api.themoviedb.org"

    // VARIABLES
    const val DEFAULT_USERNAME = "admin@gmail.com"
    const val DEFAULT_PASSWORD = "1234"

    /**
     * Cache configuration constants.
     * 
     * These values control data caching behavior across repositories.
     */
    object Cache {
        /**
         * Default Time-To-Live for cached data: 2 minutes
         */
        const val DEFAULT_TTL_MS = 2 * 60 * 1000L
        
        /**
         * Minimum interval between forced refreshes: 30 seconds
         */
        const val MIN_REFRESH_INTERVAL_MS = 30_000L
        
        /**
         * Default maximum number of pages to fetch from paginated APIs
         */
        const val MAX_PAGES_DEFAULT = 5
    }

    /**
     * Network configuration constants.
     * 
     * These values control HTTP client behavior.
     */
    object Network {
        /**
         * HTTP request timeout: 30 seconds
         */
        const val HTTP_TIMEOUT_MS = 30_000L
        
        /**
         * Maximum number of retry attempts for failed requests
         */
        const val RETRY_COUNT = 3
        
        /**
         * Maximum delay between retries: 5 seconds
         */
        const val MAX_RETRY_DELAY_MS = 5_000L
        
        /**
         * Connect timeout: 15 seconds
         */
        const val CONNECT_TIMEOUT_MS = 15_000L
        
        /**
         * Socket timeout: 30 seconds
         */
        const val SOCKET_TIMEOUT_MS = 30_000L
    }

    /**
     * UI/Animation constants.
     * 
     * These values control UI behavior and animations.
     */
    object UI {
        /**
         * Standard animation duration: 300ms
         */
        const val ANIMATION_DURATION_MS = 300
        
        /**
         * Fast animation duration: 150ms
         */
        const val ANIMATION_DURATION_FAST_MS = 150
        
        /**
         * Slow animation duration: 500ms
         */
        const val ANIMATION_DURATION_SLOW_MS = 500
        
        /**
         * Debounce delay for search inputs: 500ms
         */
        const val DEBOUNCE_MS = 500
        
        /**
         * Default page size for pagination
         */
        const val DEFAULT_PAGE_SIZE = 20
    }

    /**
     * API constants.
     * 
     * API-specific configuration values.
     */
    object Api {
        /**
         * IGDB token expiration buffer: 1 hour before actual expiration
         */
        const val TOKEN_EXPIRATION_BUFFER_MS = 60 * 60 * 1000L
    }
}