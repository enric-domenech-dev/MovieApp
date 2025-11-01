package org.lanzadera.proyectos.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import org.koin.compose.koinInject
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.ui.screens.detail.DetailView
import org.lanzadera.proyectos.ui.screens.home.HomeView
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel
import org.lanzadera.proyectos.ui.screens.login.LoginView
import org.lanzadera.proyectos.ui.screens.login.LoginViewModel
import org.lanzadera.proyectos.ui.screens.search.SearchView
import org.lanzadera.proyectos.ui.screens.settings.SettingView
import org.lanzadera.proyectos.ui.screens.chat.ChatView
import org.lanzadera.proyectos.ui.screens.profile.ProfileView
import org.lanzadera.proyectos.ui.screens.settings.SettingsViewModel
import org.lanzadera.proyectos.ui.screens.chat.ChatViewModel
import org.lanzadera.proyectos.ui.screens.profile.ProfileViewModel
import org.lanzadera.proyectos.ui.screens.search.SearchViewModel
import org.lanzadera.proyectos.ui.screens.splash.SplashView
import org.lanzadera.proyectos.utils.Constants
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import org.lanzadera.proyectos.ui.components.navComponents.AppBottomBar
import org.lanzadera.proyectos.utils.BottomNavItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import kotlinx.coroutines.launch
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import androidx.compose.material3.DrawerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import org.lanzadera.proyectos.ui.screens.detail.BookDetailView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navHost: NavHostController,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    // observe current route to show/hide and select bottom bar
    val navBackStackEntry by navHost.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // drawer state (moved to top-level so bottom bar can control it)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // derive whether drawer is open as a Compose state
    val isDrawerOpen by remember { derivedStateOf { drawerState.isOpen } }

    val bottomNavRoutes = setOf(
        Constants.Screen.Home.route,
        Constants.Screen.Search.route,
        Constants.Screen.Chat.route,
        Constants.Screen.Profile.route
    )

    val showBottomBar = currentRoute in bottomNavRoutes || isDrawerOpen

    // derive selected item from route, but if drawer is open show MENU selected
    val selectedItem = if (isDrawerOpen) BottomNavItem.MENU else BottomNavItem.fromRoute(currentRoute)

    Scaffold(
        bottomBar = {
            NavigationBottomBar(
                showBottomBar = showBottomBar,
                selectedItem = selectedItem,
                drawerState = drawerState,
                navHost = navHost,
                scope = scope
            )
        }
    ) {
        DrawerAppBar(navViewModel = navHost, drawerState = drawerState) {
            NavHost(navController = navHost, startDestination = Constants.Screen.SplashScreen.route) {
                composable(Constants.Screen.SplashScreen.route) {
                    SplashView(nav = navHost, darkTheme = darkTheme, selectedTheme = selectedTheme)
                }
                composable(Constants.Screen.Home.route) {
                    HomeView(
                        nav = navHost, vm = HomeViewModel(koinInject(), koinInject()),
                        //selectedTheme = selectedTheme, darkTheme = darkTheme
                    )
                }
                composable(Constants.Screen.Login.route) {
                    LoginView(
                        nav = navHost, vm = LoginViewModel(),
                        selectedTheme = selectedTheme, darkTheme = darkTheme
                    )
                }
                composable(Constants.Screen.Detail.route) {
                    // Show detail depending on what was selected (movie or book). Avoid !! crashes.
                    val movie = NavigationStore.selectedMovie
                    val book = NavigationStore.selectedBook
                    if (movie != null) {
                        DetailView(nav = navHost, movie = movie, selectedTheme = selectedTheme, darkTheme = darkTheme)
                    } else if (book != null) {
                        BookDetailView(nav = navHost, book = book, selectedTheme = selectedTheme, darkTheme = darkTheme)
                    } else {
                        // fallback: nothing selected — navigate back safely
                        LaunchedEffect(Unit) { navHost.popBackStack() }
                    }
                }
                composable(Constants.Screen.Search.route) {
                    SearchView(
                        nav = navHost, vm = SearchViewModel(),
                        selectedTheme = selectedTheme, darkTheme = darkTheme
                    )
                }
                composable(Constants.Screen.Settings.route) {
                    SettingView(navHost, SettingsViewModel())
                }
                composable(Constants.Screen.Chat.route) {
                    ChatView(nav = navHost, vm = ChatViewModel())
                }
                composable(Constants.Screen.Profile.route) {
                    ProfileView(nav = navHost, vm = ProfileViewModel())
                }

            }
        }
    }

}

@Composable
private fun NavigationBottomBar(
    showBottomBar: Boolean,
    selectedItem: BottomNavItem?,
    drawerState: DrawerState,
    navHost: NavHostController,
    scope: CoroutineScope
) {
    if (!showBottomBar) return

    AppBottomBar(selectedItem = selectedItem) { item ->
        if (item == BottomNavItem.MENU) {
            scope.launch {
                if (drawerState.isOpen) drawerState.close() else drawerState.open()
            }
        } else {
            scope.launch { drawerState.close() }
            navHost.navigate(item.route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navHost.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
}