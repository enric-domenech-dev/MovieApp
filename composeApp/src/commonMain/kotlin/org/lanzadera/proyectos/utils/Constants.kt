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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
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
        object Search : Screen("search")
        // Added settings/chat/profile to centralize routes and avoid literals
        object Settings : Screen("settings")
        object Chat : Screen("chat")
        object Profile : Screen("profile")
    }

    // MENU OPTIONS
    object MenuOptions {
        val topBarTitles = listOf("BOOKS", "FILMS", "SERIES", "GAMES", "<3")
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


}