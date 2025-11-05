package org.lanzadera.proyectos.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.navComponents.AppBottomBar
import org.lanzadera.proyectos.ui.screens.chat.ChatView
import org.lanzadera.proyectos.ui.screens.chat.ChatViewModel
import org.lanzadera.proyectos.ui.screens.detail.BookDetailView
import org.lanzadera.proyectos.ui.screens.detail.DetailView
import org.lanzadera.proyectos.ui.screens.detail.MovieDetailView
import org.lanzadera.proyectos.ui.screens.detail.MovieDetailViewModel
import org.lanzadera.proyectos.ui.screens.detail.SeriesDetailView
import org.lanzadera.proyectos.ui.screens.detail.SeriesDetailViewModel
import org.lanzadera.proyectos.ui.screens.games.GameDetailView
import org.lanzadera.proyectos.ui.screens.games.GameDetailViewModel
import org.lanzadera.proyectos.ui.screens.home.HomeView
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel
import org.lanzadera.proyectos.ui.screens.login.LoginView
import org.lanzadera.proyectos.ui.screens.login.LoginViewModel
import org.lanzadera.proyectos.ui.screens.profile.ProfileView
import org.lanzadera.proyectos.ui.screens.profile.ProfileViewModel
import org.lanzadera.proyectos.ui.screens.search.SearchView
import org.lanzadera.proyectos.ui.screens.search.SearchViewModel
import org.lanzadera.proyectos.ui.screens.settings.SettingView
import org.lanzadera.proyectos.ui.screens.settings.SettingsViewModel
import org.lanzadera.proyectos.ui.screens.splash.SplashView
import org.lanzadera.proyectos.utils.BottomNavItem
import org.lanzadera.proyectos.utils.Constants

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

    val detailRoutes = setOf(
        Constants.Screen.MovieDetail.route,
        Constants.Screen.SeriesDetail.route
    )

    val drawerEnabled = currentRoute !in detailRoutes

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
        DrawerAppBar(navViewModel = navHost, drawerState = drawerState, drawerEnabled = drawerEnabled) {
            NavHost(navController = navHost, startDestination = Constants.Screen.SplashScreen.route) {
                composable(Constants.Screen.SplashScreen.route) {
                    SplashView(nav = navHost, darkTheme = darkTheme, selectedTheme = selectedTheme)
                }
                composable(Constants.Screen.Home.route) {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    HomeView(
                        nav = navHost, vm = homeViewModel,
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
                        vm = koinViewModel<SearchViewModel>(),
                        navController = navHost
                    )
                }
                composable(Constants.Screen.MovieDetail.route) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                    if (movieId != null) {
                        val viewModel: MovieDetailViewModel = koinViewModel()
                        MovieDetailView(nav = navHost, viewModel = viewModel, movieId = movieId)
                    } else {
                        LaunchedEffect(Unit) { navHost.popBackStack() }
                    }
                }
                composable(Constants.Screen.SeriesDetail.route) { backStackEntry ->
                    val tvShowId = backStackEntry.arguments?.getString("tvShowId")?.toIntOrNull()
                    if (tvShowId != null) {
                        val viewModel: SeriesDetailViewModel = koinViewModel()
                        SeriesDetailView(nav = navHost, vm = viewModel, tvShowId = tvShowId)
                    } else {
                        LaunchedEffect(Unit) { navHost.popBackStack() }
                    }
                }
                composable(Constants.Screen.GameDetail.route) { backStackEntry ->
                    val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
                    if (gameId != null) {
                        val viewModel: GameDetailViewModel = koinViewModel()
                        GameDetailView(
                            gameId = gameId,
                            viewModel = viewModel,
                            onNavigateBack = { navHost.popBackStack() }
                        )
                    } else {
                        LaunchedEffect(Unit) { navHost.popBackStack() }
                    }
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