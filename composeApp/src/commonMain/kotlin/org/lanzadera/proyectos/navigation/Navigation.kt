package org.lanzadera.proyectos.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.ui.screens.detail.DetailView
import org.lanzadera.proyectos.ui.screens.home.HomeView
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel
import org.lanzadera.proyectos.ui.screens.login.LoginView
import org.lanzadera.proyectos.ui.screens.login.LoginViewModel
import org.lanzadera.proyectos.ui.screens.search.SearchView
import org.lanzadera.proyectos.ui.screens.search.SearchViewModel
import org.lanzadera.proyectos.ui.screens.splash_screen.SplashView
import org.lanzadera.proyectos.utils.Constants

@Composable
fun Navigation(
    navHost: NavHostController,
    navigation: NavigationController,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    LaunchedEffect(key1 = navigation) {
        navigation.navigationEvent.collect { event ->
            when (event) {
                Constants.NavigationEvent.NavigateToHome -> navHost.navigate(Constants.Screen.Home.route)
                Constants.NavigationEvent.NavigateToLogin -> navHost.navigate(Constants.Screen.Login.route)
                Constants.NavigationEvent.NavigateToRegister -> navHost.navigate(Constants.Screen.SignIn.route)
                Constants.NavigationEvent.NavigateToSplashScreen -> navHost.navigate(Constants.Screen.SplashScreen.route)
                Constants.NavigationEvent.NavigateToDetail -> navHost.navigate(Constants.Screen.Detail.route)
                Constants.NavigationEvent.NavigateToSearch -> navHost.navigate(Constants.Screen.Search.route)
                Constants.NavigationEvent.NavigateBack -> navHost.popBackStack()
            }
        }
    }

    NavHost(navController = navHost, startDestination = Constants.Screen.SplashScreen.route) {
        composable(Constants.Screen.SplashScreen.route) {
            SplashView(nav = navigation, darkTheme = darkTheme, selectedTheme = selectedTheme)
        }
        composable(Constants.Screen.Home.route) {
            HomeView(
                nav = navigation, vm = HomeViewModel(koinInject()),
                //selectedTheme = selectedTheme, darkTheme = darkTheme
            )
        }
        composable(Constants.Screen.Login.route) {
            LoginView(
                nav = navigation, vm = LoginViewModel(),
                selectedTheme = selectedTheme, darkTheme = darkTheme
            )
        }
        composable(Constants.Screen.Detail.route) {
            DetailView(
                nav = navigation, movie = navigation.selectedMovie,
                selectedTheme = selectedTheme, darkTheme = darkTheme
            )
        }
        composable(Constants.Screen.Search.route) {
            SearchView(
                nav = navigation, vm = SearchViewModel(),
                selectedTheme = selectedTheme, darkTheme = darkTheme
            )
        }

    }

}