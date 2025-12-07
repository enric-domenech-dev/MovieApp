# Navigation Lambda Refactoring Plan

**Date:** December 7, 2025  
**Status:** 🔴 CRITICAL - Must Fix  
**Priority:** P0

---

## Problem

**Current Architecture Violation:**
- ❌ All Composables receive `NavController` or `nav: NavHostController`
- ❌ Navigation logic scattered across UI components
- ❌ Composables are NOT testable (require NavController mock)
- ❌ Violates Compose best practices

**Example of BAD code:**
```kotlin
@Composable
fun HomeView(nav: NavHostController) {
    MovieItem(movie) {
        nav.navigate(Screen.MovieDetail(movie.id))
    }
}

@Composable
fun MovieItem(movie: MovieUI, onClick: () -> Unit) {
    // Still coupled to navigation through parent
}
```

---

## Solution

**Use Lambda Callbacks with Clear Naming:**
- ✅ NavController stays ONLY in `Navigation.kt`
- ✅ Each screen receives lambda callbacks: `onNavigateToX: (id: Int) -> Unit`
- ✅ Composables are pure and testable
- ✅ Navigation logic centralized

**Example of GOOD code:**
```kotlin
@Composable
fun HomeView(
    onNavigateToMovieDetail: (movieId: Int) -> Unit,
    onNavigateToTvShowDetail: (tvShowId: Int) -> Unit,
    onNavigateToGameDetail: (gameId: Int) -> Unit,
    onNavigateToBookDetail: (bookId: String) -> Unit,
) {
    MovieItem(movie) {
        onNavigateToMovieDetail(movie.id)
    }
}
```

---

## Refactoring Steps

### Phase 1: Update Navigation.kt (Central Hub)
- Define all navigation lambdas
- Pass lambdas to each screen
- Keep NavController private to Navigation.kt

### Phase 2: Update Main Screens
1. **HomeView** → Add navigation lambda parameters
2. **SearchView** → Add navigation lambda parameters
3. **Detail Screens** → Add `onNavigateBack: () -> Unit`
4. **SplashView** → Add `onNavigateToHome: () -> Unit`, `onNavigateToLogin: () -> Unit`
5. **LoginView** → Add `onNavigateToHome: () -> Unit`, `onNavigateBack: () -> Unit`

### Phase 3: Update Components
1. **MovieComponents.kt** → Remove nav, add lambdas
2. **TvShowComponents.kt** → Remove nav, add lambdas
3. **GameComponents.kt** → Remove nav, add lambdas
4. **BookComponents.kt** → Remove nav, add lambdas
5. **Section components** → Remove nav, add lambdas

### Phase 4: Update DrawerAppBar & BottomBar
- BottomBar should receive `onNavigateTo: (Screen) -> Unit`
- DrawerAppBar navigation through lambdas

---

## Implementation Example

### Navigation.kt (Central Hub)
```kotlin
@Composable
fun Navigation(...) {
    val navHost = rememberNavController()
    
    NavHost(navController = navHost, startDestination = Screen.SplashScreen) {
        composable<Screen.Home> {
            HomeView(
                onNavigateToMovieDetail = { movieId ->
                    navHost.navigate(Screen.MovieDetail(movieId))
                },
                onNavigateToTvShowDetail = { tvShowId ->
                    navHost.navigate(Screen.TvShowDetail(tvShowId))
                },
                onNavigateToGameDetail = { gameId ->
                    navHost.navigate(Screen.GameDetail(gameId))
                },
                onNavigateToBookDetail = { bookId ->
                    navHost.navigate(Screen.BookDetail(bookId))
                },
                onNavigateToSearch = {
                    navHost.navigate(Screen.Search)
                },
                onNavigateToChat = {
                    navHost.navigate(Screen.Chat)
                },
                onNavigateToProfile = {
                    navHost.navigate(Screen.Profile)
                },
            )
        }
        
        composable<Screen.MovieDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.MovieDetail>()
            MovieDetailView(
                movieId = args.movieId,
                onNavigateBack = { navHost.popBackStack() }
            )
        }
        
        // ... more routes
    }
}
```

### HomeView.kt (Pure Composable)
```kotlin
@Composable
fun HomeView(
    vm: HomeViewModel = koinViewModel(),
    favoritesVM: FavoritesTabViewModel = koinViewModel(),
    booksVM: BooksTabViewModel = koinViewModel(),
    filmsVM: FilmsTabViewModel = koinViewModel(),
    seriesVM: SeriesTabViewModel = koinViewModel(),
    gamesVM: GamesTabViewModel = koinViewModel(),
    // Navigation callbacks
    onNavigateToMovieDetail: (movieId: Int) -> Unit,
    onNavigateToTvShowDetail: (tvShowId: Int) -> Unit,
    onNavigateToGameDetail: (gameId: Int) -> Unit,
    onNavigateToBookDetail: (bookId: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    // No NavController here!
    // Just pass lambdas to child components
    
    when (selectedTab) {
        FAVORITES -> FavoritesTabContent(
            vm = favoritesVM,
            onMovieClick = onNavigateToMovieDetail,
            onTvShowClick = onNavigateToTvShowDetail,
        )
        FILMS -> FilmsTabContent(
            vm = filmsVM,
            onMovieClick = onNavigateToMovieDetail,
        )
        // ...
    }
}
```

### MovieComponents.kt (Pure Composable)
```kotlin
@Composable
fun MovieItem(
    movie: MovieUI,
    onClick: () -> Unit, // Generic click, no navigation knowledge
) {
    Card(onClick = onClick) {
        // UI only
    }
}

// Used like this:
MovieItem(movie) {
    onNavigateToMovieDetail(movie.id) // Lambda from parent
}
```

---

## Benefits

1. **Testability:** Composables can be tested without NavController
2. **Reusability:** Components don't depend on navigation
3. **Separation of Concerns:** Navigation logic in one place
4. **Type Safety:** Lambda signatures define contract
5. **Maintainability:** Easy to change navigation logic

---

## Files to Update

### High Priority (Main Screens)
- [ ] `navigation/Navigation.kt` - Add all navigation lambdas
- [ ] `ui/screens/home/HomeView.kt` - Remove nav, add lambdas
- [ ] `ui/screens/search/SearchView.kt` - Remove nav, add lambdas
- [ ] `ui/screens/detail/MovieDetailView.kt` - Replace nav with onNavigateBack
- [ ] `ui/screens/detail/SeriesDetailView.kt` - Replace nav with onNavigateBack
- [ ] `ui/screens/games/GameDetailView.kt` - Already has onNavigateBack ✅
- [ ] `ui/screens/detail/BookDetailView.kt` - Replace nav with onNavigateBack
- [ ] `ui/screens/splash/SplashView.kt` - Add navigation lambdas
- [ ] `ui/screens/login/LoginView.kt` - Add navigation lambdas

### Medium Priority (Components)
- [ ] `ui/components/MovieComponents.kt` - Remove nav references
- [ ] `ui/components/TvShowComponents.kt` - Remove nav references
- [ ] `ui/components/GameComponents.kt` - Remove nav references
- [ ] `ui/components/BookComponents.kt` - Remove nav references
- [ ] `ui/components/sections/*.kt` - All section components

### Low Priority (Navigation UI)
- [ ] `ui/components/DrawerAppBar.kt` - Use lambdas
- [ ] `ui/components/navComponents/AppBottomBar.kt` - Use lambdas

---

## Estimated Time

- Phase 1 (Navigation.kt): 1 hour
- Phase 2 (Main Screens): 2-3 hours
- Phase 3 (Components): 2-3 hours
- Phase 4 (Nav UI): 1 hour

**Total:** 6-8 hours

---

## Success Criteria

- ✅ NavController exists ONLY in Navigation.kt
- ✅ All screens receive lambda callbacks
- ✅ All components are pure (no nav dependency)
- ✅ Build successful
- ✅ All navigation flows work
- ✅ Code is testable

---

## Next Steps

1. Start with Navigation.kt - define all lambdas
2. Update HomeView (biggest screen)
3. Update SearchView
4. Update Detail screens
5. Update all components
6. Test thoroughly
7. Commit with message: "Refactor: Navigation with lambda callbacks (Compose best practice)"
