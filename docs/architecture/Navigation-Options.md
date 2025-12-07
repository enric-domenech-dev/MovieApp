# Navigation Architecture Options

## Current Implementation ✅ RECOMMENDED

**Pattern:** Direct Screen import in UI components

```kotlin
// navigation/Screen.kt
@Serializable
sealed interface Screen {
    @Serializable
    data class MovieDetail(val movieId: Int) : Screen
}

// ui/components/MovieComponents.kt
import org.lanzadera.proyectos.navigation.Screen

fun MovieItem(movie: MovieUI, nav: NavHostController) {
    Column(modifier = Modifier.clickable {
        nav.navigate(Screen.MovieDetail(movieId = movie.id))
    })
}
```

**Status:** ✅ Clean Architecture compliant  
**Reason:** Navigation is part of UI layer, UI can import from UI

### Pros:
- ✅ Type-safe navigation (compile-time checks)
- ✅ Simple, minimal boilerplate
- ✅ Google's official recommendation
- ✅ Direct mapping Screen → Route

### Cons:
- ⚠️ Components know about routing structure
- ⚠️ Harder to test in isolation
- ⚠️ Platform-specific (Android NavController)

---

## Alternative: Navigator Interface Pattern

**When to use:** 
- Need to support multiple platforms with different navigation (Web, Desktop, iOS)
- Want extra testability (mock Navigator)
- Components should be 100% platform-agnostic

### Implementation

```kotlin
// ui/navigation/Navigator.kt (interface in UI layer)
interface Navigator {
    fun navigateToMovieDetail(movieId: Int)
    fun navigateToTvShowDetail(tvShowId: Int)
    fun navigateToGameDetail(gameId: Int)
    fun navigateToBookDetail(bookId: String)
    fun navigateBack()
}

// ui/navigation/AndroidNavigator.kt (Android implementation)
class AndroidNavigator(
    private val navController: NavHostController
) : Navigator {
    override fun navigateToMovieDetail(movieId: Int) {
        navController.navigate(Screen.MovieDetail(movieId))
    }
    
    override fun navigateToTvShowDetail(tvShowId: Int) {
        navController.navigate(Screen.TvShowDetail(tvShowId))
    }
    
    override fun navigateBack() {
        navController.popBackStack()
    }
    
    // etc.
}

// ui/components/MovieComponents.kt
fun MovieItem(
    movie: MovieUI,
    navigator: Navigator // ← Interface, not NavHostController
) {
    Column(modifier = Modifier.clickable {
        navigator.navigateToMovieDetail(movie.id)
    })
}

// ui/navigation/Navigation.kt
@Composable
fun Navigation(navHost: NavHostController) {
    val navigator = remember { AndroidNavigator(navHost) }
    
    NavHost(navController = navHost, startDestination = Screen.SplashScreen) {
        composable<Screen.Home> {
            HomeView(navigator = navigator)
        }
        // etc.
    }
}

// Testing
class FakeNavigator : Navigator {
    val events = mutableListOf<String>()
    
    override fun navigateToMovieDetail(movieId: Int) {
        events.add("MovieDetail:$movieId")
    }
}

@Test
fun `clicking movie navigates to detail`() {
    val navigator = FakeNavigator()
    val movie = MovieUI(id = 123, title = "Test")
    
    composeTestRule.setContent {
        MovieItem(movie, navigator)
    }
    
    composeTestRule.onNodeWithText("Test").performClick()
    
    assertThat(navigator.events).contains("MovieDetail:123")
}
```

### Pros:
- ✅ Components fully decoupled from NavController
- ✅ Easy to test (mock/fake Navigator)
- ✅ Platform-agnostic (could have WebNavigator, DesktopNavigator)
- ✅ Can log/intercept all navigation events

### Cons:
- ⚠️ Extra abstraction layer
- ⚠️ Boilerplate: need method for each route
- ⚠️ Loses some type-safety (methods vs sealed class)
- ⚠️ More files to maintain

---

## Decision Matrix

| Criterion | Current (Direct) | Navigator Interface |
|-----------|-----------------|---------------------|
| Type Safety | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Simplicity | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Testability | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Multi-platform | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Boilerplate | ⭐⭐⭐⭐⭐ (minimal) | ⭐⭐⭐ (more) |
| Google's Recommendation | ✅ Yes | ⚠️ Optional |

## Recommendation

### For This Project: Keep Current ✅

**Why:**
1. **It's a mobile-first KMP app** - Navigation is platform-specific anyway
2. **Type-safety is valuable** - Screen sealed class catches errors at compile time
3. **Pragmatic** - Don't over-engineer for theoretical benefits
4. **Google's pattern** - Official samples do this
5. **Testing is still possible** - Can test ViewModels and use cases separately

### When to Consider Navigator Interface:

1. **Multi-platform with different navigation:**
   - Web app with URL-based routing
   - Desktop with window management
   - iOS with UINavigationController

2. **Heavy UI testing requirements:**
   - Need to mock navigation in lots of UI tests
   - Want to verify navigation without rendering screens

3. **Complex navigation logic:**
   - Need to intercept/log all navigation
   - Want to validate navigation conditions
   - Need to handle deep links centrally

## Conclusion

**Current implementation is Clean Architecture compliant ✅**

- Navigation is part of UI layer
- UI components can import Screen
- No violation of dependency rules
- Follows Google's best practices

**Only refactor to Navigator interface if:**
- Team consensus that benefits > costs
- Real need for platform-agnostic components
- UI testing becomes a bottleneck

For now, **keep the current approach** - it's simple, type-safe, and correct.
