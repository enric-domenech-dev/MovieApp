package org.lanzadera.proyectos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.lanzadera.proyectos.utils.Constants
import org.lanzadera.proyectos.ui.screens.home.HomeView
import org.lanzadera.proyectos.ui.screens.login.LoginView

@Composable
fun Navigation(
    navHost: NavHostController,
    navigation: NavigationController,
) {

    LaunchedEffect(key1 = navigation) {
        navigation.navigationEvent.collect { event ->
            when (event) {
                Constants.NavigationEvent.NavigateToHome -> navHost.navigate(Constants.Screen.Home.route)
                Constants.NavigationEvent.NavigateToLogin -> navHost.navigate(Constants.Screen.Login.route)
                Constants.NavigationEvent.NavigateToRegister -> navHost.navigate(Constants.Screen.SignIn.route)
                Constants.NavigationEvent.NavigateToSplashScreen -> navHost.navigate(Constants.Screen.SplashScreen.route)
                Constants.NavigationEvent.NavigateBack -> navHost.popBackStack()
            }
        }
    }

    NavHost(navController = navHost, startDestination = Constants.Screen.Login.route) {
        composable(Constants.Screen.SplashScreen.route) {
            // SplashScreen(navController = navHost)
        }
        composable(Constants.Screen.Home.route) {
            HomeView(navigation = navigation)
        }
        composable(Constants.Screen.Login.route) {
            LoginView(navigation = navigation)
        }


    }

}