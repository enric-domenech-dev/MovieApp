# COPILOT.md

This file provides guidance to Copilot when working with code in this repository.

---

## 🚀 SESSION INITIALIZATION PROTOCOL

### ⚡ MANDATORY - Read ALL Documentation Before Starting

**CRITICAL:** Every Copilot session MUST execute these commands FIRST:

```bash
# ═══════════════════════════════════════════════════════════
# 📖 STEP 1: Read Core Project Documentation (REQUIRED)
# ═══════════════════════════════════════════════════════════

cat README.md                    # Project overview and quick start
cat COPILOT.md                   # This file - architecture rules
cat TODO_LIST.md                 # Current progress and next task
cat AUDIT_REPORT.md              # Code audit findings

# ═══════════════════════════════════════════════════════════
# 🏗️ STEP 2: Read Architecture Decision Records (REQUIRED)
# ═══════════════════════════════════════════════════════════

cat docs/architecture/ADR-001-Clean-Architecture.md
cat docs/architecture/ADR-002-Use-Case-Layer.md
cat docs/architecture/ADR-003-Repository-Pattern.md
cat docs/architecture/ADR-004-DTO-vs-Domain-Models.md

# ═══════════════════════════════════════════════════════════
# 🧪 STEP 3: Read Testing Documentation (REQUIRED)
# ═══════════════════════════════════════════════════════════

cat copilot/TESTING_STRATEGY.md
cat copilot/TESTING_COVERAGE.md
cat copilot/copilot-instructions.md

# ═══════════════════════════════════════════════════════════
# 📊 STEP 4: Check Current Status (REQUIRED)
# ═══════════════════════════════════════════════════════════

grep -A 3 "Next Task:" TODO_LIST.md
grep "Overall Progress:" TODO_LIST.md
grep -A 5 "Current Sprint:" TODO_LIST.md
```

### Why This Is MANDATORY

Reading all documentation ensures:

1. ✅ **No Architecture Violations** - You understand Clean Architecture rules
2. ✅ **No Duplicate Work** - You know what's already done
3. ✅ **Correct Task** - You work on the right task in sequence
4. ✅ **Consistent Code** - You follow established patterns
5. ✅ **No Regressions** - You know about past bugs and their fixes

### Session Start Protocol

**📝 User says:**
> "Let's continue - initialize session"

**🤖 Copilot MUST:**
1. ✅ Execute ALL commands above to read documentation
2. ✅ Identify the current phase and next task from TODO_LIST.md
3. ✅ Confirm understanding of the task
4. ✅ Ask for clarification if needed
5. ✅ Only then proceed with implementation

### ⚠️ DO NOT START CODING WITHOUT READING DOCS

**Why?** Past sessions showed that skipping documentation leads to:
- ❌ Architecture violations (UI importing domain models)
- ❌ Wrong patterns (multiple public methods in use cases)
- ❌ Bugs (removing @Serializable without tests)
- ❌ Duplicate work (re-implementing existing code)

---

**📚 Required Documentation Files:**

### Core Files (MUST READ EVERY SESSION)
1. [README.md](./README.md) - Project overview, structure, quick start
2. [COPILOT.md](./COPILOT.md) - This file - architecture rules and conventions
3. [TODO_LIST.md](./TODO_LIST.md) - Detailed task list (140 tasks)
4. [AUDIT_REPORT.md](./AUDIT_REPORT.md) - Code audit with identified issues

### Architecture Decision Records (MUST READ EVERY SESSION)
5. [ADR-001: Clean Architecture](docs/architecture/ADR-001-Clean-Architecture.md)
6. [ADR-002: Use Case Layer](docs/architecture/ADR-002-Use-Case-Layer.md)
7. [ADR-003: Repository Pattern](docs/architecture/ADR-003-Repository-Pattern.md)
8. [ADR-004: DTO vs Domain Models](docs/architecture/ADR-004-DTO-vs-Domain-Models.md)

### Testing Documentation (MUST READ EVERY SESSION)
9. [TESTING_STRATEGY.md](copilot/TESTING_STRATEGY.md) - Testing approach and guidelines
10. [TESTING_COVERAGE.md](copilot/TESTING_COVERAGE.md) - Coverage configuration
11. [copilot-instructions.md](copilot/copilot-instructions.md) - How to work with Copilot

### Reference Documentation (READ AS NEEDED)
- [DTO_Migration_Plan.md](docs/architecture/DTO_Migration_Plan.md) - DTO migration guide
- [HomeViewModel_Refactoring_Plan.md](docs/architecture/HomeViewModel_Refactoring_Plan.md) - ViewModel refactoring
- [Why_Tests_Didnt_Catch_Bugs.md](docs/analysis/Why_Tests_Didnt_Catch_Bugs.md) - Testing lessons learned

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

### Testing Strategy

Tests use **Turbine** for Flow testing and **kotlin.test** for assertions:

```kotlin
// Flow testing pattern
repository.observeFavorites().test {
    assertEquals(expected, awaitItem())
}
```

### Test Types

1. **Unit Tests (with Fakes)** - `commonTest/`
   - ViewModels: Test business logic with fake repositories
   - Use Cases: Test use case logic with fakes
   - Example: `FakeFavoritesRepository`, `FakeMovieRepository`

2. **DTO Serialization Tests** - `commonTest/data/mapper/`
   - **CRITICAL:** Prevent serialization bugs (Task 1.9, 1.10)
   - Test JSON round-trip: DTO → JSON → DTO
   - Test domain mapping: DTO → Domain
   - Test field preservation: No data loss
   - Example: `MovieMapperTest`, `TvShowMapperTest`

3. **Integration Tests** - Coming in Phase 3
   - Test real repository implementations
   - Test HTTP deserialization
   - Test Room persistence

### DTO Testing Pattern (MANDATORY for All DTOs)

```kotlin
@Test
fun `DTO can be serialized and deserialized`() {
    // Given
    val dto = MyDto(id = 1, name = "Test")
    
    // When - Serialize to JSON
    val jsonString = json.encodeToString(dto)
    
    // Then - Deserialize back
    val decoded = json.decodeFromString<MyDto>(jsonString)
    assertEquals(dto.id, decoded.id)
}

@Test
fun `DTO to Domain mapping preserves all fields`() {
    // Given
    val dto = MyDto(id = 1, name = "Test")
    
    // When
    val domain = dto.toDomain()
    
    // Then - Verify all fields mapped
    assertEquals(dto.id, domain.id)
    assertEquals(dto.name, domain.name)
}
```

**Why This Matters:** These tests catch bugs where:
- Removing `@Serializable` breaks serialization
- Field mappings lose data (e.g., `voteAverage` → `voteAverageDouble`)
- Complex nested objects fail to serialize (e.g., Season with episodes)

### Running Tests

```bash
# Run all tests
./gradlew composeApp:testDebugUnitTest

# Run specific test class
./gradlew composeApp:testDebugUnitTest --tests "*MapperTest"

# Run with coverage
./gradlew composeApp:koverHtmlReport
```

Fake implementations exist in `commonTest/fakes/` and `commonTest/domain/`.

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

---

## 🔄 StateFlow Pattern - ALWAYS Use .asStateFlow()

### Rule: ALL Exposed StateFlows MUST Use .asStateFlow()

**WHY:** `.asStateFlow()` provides encapsulation and prevents external callers from casting back to `MutableStateFlow`.

### Pattern in Repositories:

```kotlin
// ✅ CORRECT
class MovieRepositoryImpl : MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _movies.asStateFlow()
}

// ❌ WRONG - Direct assignment allows external modification
class MovieRepositoryImpl : MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _movies  // ❌ BAD
}
```

### Pattern in ViewModels:

```kotlin
// ✅ CORRECT
class HomeViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(HomeTab.FAVORITES)
    val selectedTab: StateFlow<HomeTab> = _selectedTab.asStateFlow()
}

// ❌ WRONG
class HomeViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(HomeTab.FAVORITES)
    val selectedTab: StateFlow<HomeTab> = _selectedTab  // ❌ BAD
}
```

### Required Import:

```kotlin
import kotlinx.coroutines.flow.asStateFlow
```

### Benefits:

1. **Immutability:** External code cannot modify the flow
2. **Encapsulation:** Implementation details hidden
3. **Type Safety:** Prevents casting to MutableStateFlow
4. **Best Practice:** Kotlin official recommendation

---
