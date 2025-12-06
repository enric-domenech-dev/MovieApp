# COPILOT.md

This file provides guidance to Copilot when working with code in this repository.

---

## 🚀 SESSION INITIALIZATION PROTOCOL

**⚡ ALWAYS START EVERY SESSION WITH THIS COMMAND:**

```bash
cat COPILOT.md TODO_LIST.md
```

**Why?** This ensures:
1. ✅ You read the latest architecture rules and conventions
2. ✅ You see the current TODO progress and next task
3. ✅ You understand the project context from the start
4. ✅ You avoid making mistakes already documented

**📝 User should say at session start:**
> "Let's continue - initialize session"

**🤖 Copilot will then:**
1. Read COPILOT.md and TODO_LIST.md
2. Identify the next uncompleted task
3. Confirm the task to work on
4. Proceed with implementation

---

**📚 Required Reading (read these files at session start):**

1. [COPILOT.md](./COPILOT.md) - Architecture, build commands, and project structure
2. [copilot-instructions.md](./.github/copilot-instructions.md) - How to interact with Copilot in this project
3. [AUDIT_REPORT.md](./AUDIT_REPORT.md) - Comprehensive code audit with identified issues
4. [TODO_LIST.md](./TODO_LIST.md) - Detailed task list to reach 100/100 score

---

## ⚠️ CRITICAL RULES - READ FIRST

### 1. ALWAYS Add Imports When Using New Classes

**RULE:** Whenever you use a new class, interface, or function in a file, you MUST:
1. Add the corresponding import at the top of the file
2. Verify the import respects Clean Architecture dependency rules
3. Ensure the import compiles successfully

**Clean Architecture Import Rules:**

✅ **ALLOWED:**
```kotlin
// domain/ can import ONLY:
import kotlinx.coroutines.*           // ✅ Kotlin stdlib
import kotlinx.datetime.*             // ✅ Kotlin libraries
// NO OTHER IMPORTS in domain/models or domain/repository

// domain/usecase can import:
import org.lanzadera.proyectos.domain.models.*      // ✅ Domain models
import org.lanzadera.proyectos.domain.repository.*  // ✅ Domain interfaces

// data/ can import:
import org.lanzadera.proyectos.domain.*  // ✅ Domain layer (interfaces & models)
import io.ktor.client.*                   // ✅ Framework libs
import androidx.room.*                    // ✅ Platform libs

// ui/ can import ONLY:
import androidx.compose.*                 // ✅ UI framework
import org.koin.*                         // ✅ DI framework
// UI should use ViewModels which use UseCases
// UI should have its own UI models (data classes)
```

❌ **FORBIDDEN:**
```kotlin
// domain/ CANNOT import:
import org.lanzadera.proyectos.data.*   // ❌ Domain can't depend on data
import org.lanzadera.proyectos.ui.*     // ❌ Domain can't depend on UI
import io.ktor.client.*                  // ❌ Domain can't depend on frameworks
import androidx.room.*                   // ❌ Domain can't depend on platform

// data/ CANNOT import:
import org.lanzadera.proyectos.ui.*     // ❌ Data can't depend on UI

// ui/ CANNOT import:
import org.lanzadera.proyectos.domain.models.*  // ❌ UI should NOT use domain models directly
import org.lanzadera.proyectos.domain.repository.*  // ❌ UI should NOT use repositories
import org.lanzadera.proyectos.data.*   // ❌ UI can't depend on data layer
```

**Correct Flow:**
```
Domain Model → Mapper → UI Model
     ↓
  UseCase (returns domain model)
     ↓
  ViewModel (maps to UI model)
     ↓
  Composable (uses UI model)
```

**Example - CORRECT Way:**
```kotlin
// domain/models/Movie.kt
data class Movie(val id: Int, val title: String, ...) // Domain model

// ui/models/MovieUI.kt
data class MovieUI(val id: Int, val title: String, ...) // UI model

// ui/mapper/MovieMapper.kt
fun Movie.toUI() = MovieUI(id = id, title = title, ...)

// ui/viewmodel/MoviesViewModel.kt
class MoviesViewModel(private val useCase: GetMoviesUseCase) {
    val movies: StateFlow<List<MovieUI>> = useCase()
        .map { domainMovies -> domainMovies.map { it.toUI() } }
        .stateIn(...)
}

// ui/screens/MoviesScreen.kt
@Composable
fun MoviesScreen(viewModel: MoviesViewModel) {
    val movies = viewModel.movies.collectAsState()
    // Use MovieUI, not Movie
}
```

**Example - WRONG Way:**
```kotlin
// ui/screens/MoviesScreen.kt - ❌ WRONG
import org.lanzadera.proyectos.domain.models.Movie // ❌ NO!

@Composable
fun MoviesScreen(movies: List<Movie>) { // ❌ Using domain model in UI
    // This breaks Clean Architecture!
}
```

**How to Check:**
1. Check which layer the file is in (domain/, data/, ui/)
2. Verify dependency direction: UI → ViewModel → UseCase → Repository → DataSource
3. UI should NEVER import domain.models or domain.repository
4. Create UI models (DTOs) and mappers
5. Compile after adding imports

### ⚠️ CRITICAL RULE: Never Use Qualified Names in Code

**❌ WRONG - Using qualified names:**
```kotlin
val item = org.lanzadera.proyectos.domain.models.FavoriteItem(...)
val type = org.lanzadera.proyectos.ui.models.FavoriteType.MOVIE
```

**✅ CORRECT - Import first, then use directly:**
```kotlin
import org.lanzadera.proyectos.domain.models.FavoriteItem
import org.lanzadera.proyectos.ui.models.FavoriteType

val item = FavoriteItem(...)
val type = FavoriteType.MOVIE
```

**When class names conflict, use type aliases:**
```kotlin
import org.lanzadera.proyectos.domain.models.FavoriteItem as DomainFavoriteItem
import org.lanzadera.proyectos.ui.models.FavoriteItem as UIFavoriteItem

val domainItem = DomainFavoriteItem(...)
val uiItem = UIFavoriteItem(...)
```

### 2. Use Cases Must Have Only One Public Method

**RULE:** A use case should follow Single Responsibility Principle and have **only one public method**: `operator fun invoke(...)`

**❌ WRONG - Multiple public methods:**
```kotlin
class ObserveWatchedEpisodesUseCase(
    private val repository: WatchedEpisodesRepository
) {
    operator fun invoke(tvShowId: String): Flow<List<WatchedEpisode>> {
        return repository.observeWatchedEpisodes(tvShowId)
    }
    
    fun observeAll(): Flow<List<WatchedEpisode>> { // ❌ Second public method!
        return repository.observeAllWatchedEpisodes()
    }
}
```

**✅ CORRECT - Separate use cases:**
```kotlin
// Use case 1: Observe episodes for a specific show
class ObserveWatchedEpisodesUseCase(
    private val repository: WatchedEpisodesRepository
) {
    operator fun invoke(tvShowId: String): Flow<List<WatchedEpisode>> {
        return repository.observeWatchedEpisodes(tvShowId)
    }
}

// Use case 2: Observe all episodes
class ObserveAllWatchedEpisodesUseCase(
    private val repository: WatchedEpisodesRepository
) {
    operator fun invoke(): Flow<List<WatchedEpisode>> {
        return repository.observeAllWatchedEpisodes()
    }
}
```

**Why?** 
- Single Responsibility Principle
- Easier to test
- Clearer naming and intent
- Better maintainability

### 3. ViewModel-Per-Tab Pattern for Complex Screens

**RULE:** When a screen has multiple tabs with independent data and logic, create separate ViewModels for each tab.

**❌ WRONG - Single God ViewModel:**
```kotlin
// HomeViewModel.kt - 638 lines, 9 dependencies, 60+ StateFlows
class HomeViewModel(
    private val moviesRepo: MovieRepository,
    private val tvShowsRepo: TvShowRepository,
    private val booksRepo: BooksRepository,
    private val gamesRepo: GameRepository,
    private val favoritesRepo: FavoritesRepository,
    // ... 4 more repos
) : ViewModel() {
    // 60+ StateFlows for all tabs
    val popularMovies: StateFlow<List<MovieUI>>
    val trendingBooks: StateFlow<List<BookUI>>
    val favoriteTvShows: StateFlow<List<TvShowUI>>
    // ... 57 more StateFlows
}
```

**✅ CORRECT - ViewModel per tab:**
```kotlin
// HomeViewModel.kt - 58 lines, 0 dependencies, 1 StateFlow
class HomeViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(HomeTab.FAVORITES)
    val selectedTab: StateFlow<HomeTab> = _selectedTab.asStateFlow()
    
    fun selectTab(tab: HomeTab) {
        _selectedTab.value = tab
    }
}

// FavoritesTabViewModel.kt - 318 lines, 5 dependencies, 8 StateFlows
class FavoritesTabViewModel(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val getFavoriteDetailsUseCase: GetFavoriteDetailsUseCase,
    // ... only favorites-related deps
) : ViewModel() {
    val favorites: StateFlow<List<FavoriteItemUI>>
    val moviesWithReleaseInfo: StateFlow<List<Pair<MovieUI, ReleaseInfoUI?>>>
    // ... only favorites-related state
}

// FilmsTabViewModel.kt - 62 lines, 1 dependency, 9 StateFlows
class FilmsTabViewModel(
    private val getInitialDataUseCase: GetInitialDataUseCase
) : ViewModel() {
    val popularMovies: StateFlow<List<MovieUI>>
    val trendingMovies: StateFlow<List<MovieUI>>
    // ... only films-related state
}

// BooksTabViewModel.kt, SeriesTabViewModel.kt, GamesTabViewModel.kt
// Similar pattern - one ViewModel per tab
```

**Update HomeView to inject all ViewModels:**
```kotlin
@Composable
fun HomeView(
    homeViewModel: HomeViewModel = koinViewModel(),
    favoritesViewModel: FavoritesTabViewModel = koinViewModel(),
    filmsViewModel: FilmsTabViewModel = koinViewModel(),
    seriesViewModel: SeriesTabViewModel = koinViewModel(),
    booksViewModel: BooksTabViewModel = koinViewModel(),
    gamesViewModel: GamesTabViewModel = koinViewModel()
) {
    val selectedTab by homeViewModel.selectedTab.collectAsState()
    
    when (selectedTab) {
        HomeTab.FAVORITES -> FavoritesTabContent(favoritesViewModel)
        HomeTab.FILMS -> FilmsTabContent(filmsViewModel)
        HomeTab.SERIES -> SeriesTabContent(seriesViewModel)
        HomeTab.BOOKS -> BooksTabContent(booksViewModel)
        HomeTab.GAMES -> GamesTabContent(gamesViewModel)
    }
}
```

**Benefits:**
- **Performance**: Only active tab ViewModel initializes (lazy loading with `SharingStarted.Lazily`)
- **Testability**: Each ViewModel has 1-5 dependencies vs 9
- **Maintainability**: 62-318 lines per file vs 638 lines
- **Single Responsibility**: Each ViewModel handles one tab's logic
- **Memory**: 83% fewer StateFlows on app startup (1 vs 60+)

**When to apply:**
- Screen has 3+ tabs with independent data sources
- ViewModel > 400 lines
- ViewModel has 5+ dependencies
- Different tabs load data at different times

---

## Build & Run Commands

```bash
# Android
./gradlew assembleDebug          # Build debug APK
./gradlew installDebug           # Install on connected device/emulator

# Tests
./gradlew testDebug              # Run Android unit tests
./gradlew :composeApp:allTests   # Run all multiplatform tests

# Desktop (Compose Desktop)
./gradlew :composeApp:run        # Run desktop app

# Full build
./gradlew build                  # Build all variants
```

## Project Context

This is a **Kotlin Multiplatform Compose** application.

**Current Project Health:**
- Overall Score: 72/100
- Architecture & Design: 75/100
- Code Quality: 65/100
- Testing & Coverage: 45/100 (~5% coverage)
- Best Practices: 80/100

- **Repository Pattern**: Domain defines interfaces, Data implements them
- **Use Cases**: Single-responsibility operations (e.g., `ToggleFavoriteUseCase`, `ObserveFavoritesUseCase`)
- **StateFlow**: ViewModels expose UI state via `StateFlow`, repositories use `Flow`
- **Koin DI**: All dependencies registered in `AppModule.kt`, injected via constructor
- **Provider Functions**: Platform-specific data sources use `expect/actual` provider functions in `datasource/`

### Data Layer

- **Room Database** (Android): Favorites, watched movies/episodes in `composeApp/schemas/`
- **In-Memory Fallback**: Non-Android platforms use in-memory implementations
- **HTTP Clients**: Ktor with named clients for TMDB, Google Books, IGDB APIs

### External APIs

| API          | Purpose          | Auth             |
|--------------|------------------|------------------|
| TMDB         | Movies, TV Shows | Bearer token     |
| Google Books | Books search     | None             |
| IGDB         | Games            | Client ID/Secret |

Credentials stored in `local.properties` (not in VCS):

Credentials stored in `local.properties` (not in VCS):
```properties
API_BEARER_TOKEN=your_tmdb_token
IGDB_CLIENT_ID=your_client_id
IGDB_CLIENT_SECRET=your_secret
```

## Logging

The project uses **Napier** for multiplatform logging.

**DO NOT use `println()` in production code.**

### Usage:

```kotlin
import org.lanzadera.proyectos.utils.Logger

// Debug logs (development only)
Logger.d("Fetching movies from API", tag = "MovieRepository")

// Info logs
Logger.i("Cache hit for movie list", tag = "MovieRepository")

// Warning logs
Logger.w("API rate limit approaching", tag = "MovieRepository")

// Error logs with exception
Logger.e("Failed to fetch movies", tag = "MovieRepository", throwable = exception)
```

### Log Levels:
- `Logger.v()` - Verbose (lowest priority)
- `Logger.d()` - Debug (for development)
- `Logger.i()` - Info (general information)
- `Logger.w()` - Warning (potential issues)
- `Logger.e()` - Error (actual errors)

### Platform Behavior:
- **Android**: Uses `android.util.Log`
- **iOS**: Uses `NSLog`
- **Desktop**: Uses formatted console output
- **Release builds**: Logs can be disabled via Napier configuration

## Testing

Tests use **Turbine** for Flow testing and **Truth** for assertions:

```kotlin
// Flow testing pattern
repository.observeFavorites().test {
    assertEquals(expected, awaitItem())
}
```

Fake implementations exist in `commonTest/` (e.g., `FakeFavoritesRepository`).

## Feature Organization

Each feature (Movies, TV Shows, Books, Games) follows the same structure:

- `domain/models/{feature}/` - Domain models
- `domain/repository/{Feature}Repository.kt` - Interface
- `data/repository/{Feature}RepositoryImpl.kt` - Implementation
- `ui/screens/{feature}/` - View + ViewModel

Favorites and watched tracking work across all content types via `FavoriteType` enum.

## Adding New Features

1. Define domain model in `domain/models/`
2. Create repository interface in `domain/repository/`
3. Implement repository in `data/repository/`
4. Add use cases in `domain/usecase/` if needed
5. Register in `AppModule.kt`
6. Create screen + ViewModel in `ui/screens/`
7. Create DTO to represent domain models in presentation layer '`ui/`
