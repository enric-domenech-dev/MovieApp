package org.lanzadera.proyectos.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.navComponents.AppBottomBar
import org.lanzadera.proyectos.ui.screens.chat.ChatView
import org.lanzadera.proyectos.ui.screens.chat.ChatViewModel
import org.lanzadera.proyectos.ui.screens.detail.BookDetailView
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
import org.lanzadera.proyectos.navigation.NavigationStore

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

    // Determine if current screen is a detail screen
    val isDetailScreen = currentRoute?.contains("MovieDetail") == true ||
                        currentRoute?.contains("TvShowDetail") == true ||
                        currentRoute?.contains("GameDetail") == true ||
                        currentRoute?.contains("BookDetail") == true

    val drawerEnabled = !isDetailScreen

    // Bottom bar visible on main screens (Home, Search, Chat, Profile)
    val isMainScreen = currentRoute?.contains("Home") == true ||
                       currentRoute?.contains("Search") == true ||
                       currentRoute?.contains("Chat") == true ||
                       currentRoute?.contains("Profile") == true
    
    val showBottomBar = isMainScreen || isDrawerOpen

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
            NavHost(navController = navHost, startDestination = Screen.SplashScreen) {
                composable<Screen.SplashScreen> {
                    SplashView(nav = navHost, darkTheme = darkTheme, selectedTheme = selectedTheme)
                }
                composable<Screen.Home> {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    HomeView(nav = navHost, vm = homeViewModel)
                }
                composable<Screen.Login> {
                    LoginView(
                        nav = navHost, vm = LoginViewModel(),
                        selectedTheme = selectedTheme, darkTheme = darkTheme
                    )
                }
                composable<Screen.Search> {
                    SearchView(
                        vm = koinViewModel<SearchViewModel>(),
                        navController = navHost
                    )
                }
                composable<Screen.MovieDetail> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.MovieDetail>()
                    val viewModel: MovieDetailViewModel = koinViewModel()
                    MovieDetailView(nav = navHost, viewModel = viewModel, movieId = args.movieId)
                }
                composable<Screen.TvShowDetail> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.TvShowDetail>()
                    val viewModel: SeriesDetailViewModel = koinViewModel()
                    SeriesDetailView(nav = navHost, vm = viewModel, tvShowId = args.tvShowId)
                }
                composable<Screen.GameDetail> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.GameDetail>()
                    val viewModel: GameDetailViewModel = koinViewModel()
                    GameDetailView(
                        gameId = args.gameId,
                        viewModel = viewModel,
                        onNavigateBack = { navHost.popBackStack() }
                    )
                }
                composable<Screen.BookDetail> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.BookDetail>()
                    // TODO: Load book by ID when repository supports it
                    // For now, books are passed via NavigationStore since there's no detail endpoint
                    val book = NavigationStore.selectedBook
                    BookDetailView(
                        nav = navHost,
                        bookId = args.bookId,
                        book = book,
                        selectedTheme = selectedTheme,
                        darkTheme = darkTheme
                    )
                }
                composable<Screen.Settings> {
                    SettingView(navHost, SettingsViewModel())
                }
                composable<Screen.Chat> {
                    ChatView(nav = navHost, vm = ChatViewModel())
                }
                composable<Screen.Profile> {
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
            // Map BottomNavItem to Screen
            val destination = when (item) {
                BottomNavItem.HOME -> Screen.Home
                BottomNavItem.SEARCH -> Screen.Search
                BottomNavItem.CHAT -> Screen.Chat
                BottomNavItem.PROFILE -> Screen.Profile
                BottomNavItem.MENU -> return@AppBottomBar // Already handled above
            }
            navHost.navigate(destination) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navHost.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
}