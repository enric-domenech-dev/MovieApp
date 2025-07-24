package org.lanzadera.proyectos.utils

object Constants {

    // SCREENS
    sealed class Screen(val route: String) {
        object Home : Screen("home")
        object Login : Screen("login")
        object SignIn : Screen("signIn")
        object SplashScreen : Screen("splashScreen")
        object Detail : Screen("detail")
        object Search : Screen("search")
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
    }

    //APY KEYS
    val API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
    val BASE_URL = "https://api.themoviedb.org"

    // VARIABLES
    const val DEFAULT_USERNAME = "admin@gmail.com"
    const val DEFAULT_PASSWORD = "1234"


}