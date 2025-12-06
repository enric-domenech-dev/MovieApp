# 📋 TODO LIST - Path to 100/100 Score

**Project:** MovieApp - Kotlin Multiplatform  
**Current Score:** 72/100  
**Target Score:** 100/100  
**Created:** December 6, 2025  
**Last Updated:** December 6, 2025

---

## 🔄 HOW TO USE THIS DOCUMENT

### For Each Copilot Session:

1. **Start:** Look for the first `[ ]` (uncompleted) task in the current active phase
2. **Work:** Follow the subtasks step by step, checking off each `[ ]` as you complete it
3. **Verify:** Ensure all acceptance criteria are met before marking task complete
4. **Mark Done:** Change `[ ]` to `[x]` for completed tasks
5. **Update Progress:** Update the "Overall Progress" counter above
6. **Commit:** Use format `"Task X.Y: [description]"` in commit messages

### Session Size Recommendations:

- **Short session (30-60 min):** 1 task from Phase 0-2
- **Medium session (1-2 hours):** 1-2 tasks from Phase 0-2, or 1 task from Phase 3-5
- **Long session (2-4 hours):** 2-3 tasks from any phase

### Progress Updates:

After completing each task, update:

- Phase completion counter (e.g., `0/8` → `1/8`)
- Overall progress counter
- Mark phase checkbox `[x]` when all phase tasks complete

---

## 🎯 SCORE TARGETS

| Category              | Current | Target  | Gap | Status         |
|-----------------------|---------|---------|-----|----------------|
| Architecture & Design | 75/100  | 100/100 | +25 | 🟡 In Progress |
| Code Quality          | 65/100  | 100/100 | +35 | 🔴 Needs Work  |
| Testing & Coverage    | 45/100  | 100/100 | +55 | 🔴 Critical    |
| Best Practices        | 80/100  | 100/100 | +20 | 🟢 Good        |

---

## 📊 PROGRESS TRACKING

### Overall Progress: 9/136 tasks completed (6.62%)

### Phase Status:

- [x] **Phase 0: Quick Wins** (8/8 completed) ✅ **COMPLETE**
- [ ] **Phase 1: Architecture Fixes** (1/16 completed) - 🎯 **CURRENT** (Task 1.1.5 CRITICAL added)
- [ ] **Phase 2: Code Quality** (0/12 completed) - Week 2
- [ ] **Phase 3: Testing - Use Cases** (0/15 completed) - Week 3
- [ ] **Phase 4: Testing - Repositories** (0/12 completed) - Week 4
- [ ] **Phase 5: Testing - ViewModels** (0/22 completed) - Weeks 5-6
- [ ] **Phase 6: UI Testing** (0/18 completed) - Week 7
- [ ] **Phase 7: Integration & Polish** (0/15 completed) - Week 8
- [ ] **Phase 8: Best Practices** (0/18 completed) - Week 8

### Current Sprint:

**Active Phase:** Phase 1 - Architecture Fixes  
**Next Task:** Task 1.1 - Extract Repository Interfaces  
**Estimated Time Remaining in Phase:** ~20-25 hours

---

## 📝 SESSION LOG

### Session History:

<!-- Update this after each session -->

- **Session 1** (Dec 6, 2025): Created TODO_LIST.md and AUDIT_REPORT.md

---

# 🚀 PHASE 0: QUICK WINS (Week 1)

**Priority:** P0 - CRITICAL  
**Goal:** Immediate improvements with minimal effort  
**Estimated Time:** 12-15 hours

## Task 0.1: Setup Logging Framework

**Impact:** HIGH | **Effort:** 2 hours | **Owner:** `___________`

### Subtasks:

- [ ] 0.1.1 Add Napier dependency to `build.gradle.kts`
  ```kotlin
  commonMain.dependencies {
      implementation("io.github.aakira:napier:2.7.1")
  }
  ```
- [ ] 0.1.2 Initialize Napier in `AppModule.kt`
  ```kotlin
  // Android
  if (BuildConfig.DEBUG) {
      Napier.base(DebugAntilog())
  }
  ```
- [ ] 0.1.3 Create Logger utility wrapper
    - File: `composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/utils/Logger.kt`
    - Functions: `logD()`, `logE()`, `logW()`, `logI()`
- [ ] 0.1.4 Document logging guidelines in `COPILOT.md`
- [ ] 0.1.5 Test logging on Android, iOS, Desktop

**Acceptance Criteria:**

- ✅ Napier installed and working on all platforms
- ✅ Logger utility created with tag support
- ✅ Documentation updated

---

## Task 0.2: Remove All println() Statements

**Impact:** HIGH | **Effort:** 3-4 hours | **Owner:** `___________`

### Subtasks:

- [ ] 0.2.1 Replace println in `ToggleFavoriteUseCase.kt` (6 occurrences)
    - Line 33: `println("ToggleFavorite: Guardando serie...")` → `Napier.d("ToggleFavorite: Guardando serie...")`
    - Lines 37, 42, 53, 57, 61: Similar replacements

- [ ] 0.2.2 Replace println in `MovieRepositoryImpl.kt` (10 occurrences)
    - Lines 62, 64, 67, 71, 106, 147, 149
    - Change to `Napier.d { "MovieRepository: ..." }`

- [ ] 0.2.3 Replace println in `HomeViewModel.kt` (11 occurrences)
    - Lines 179, 187, 193, 196, 399, 403, 416, 418, 422, 431
    - Change to `Napier.d { "HomeViewModel: ..." }`

- [ ] 0.2.4 Replace println in `TvShowRepositoryImpl.kt` (6 occurrences)
- [ ] 0.2.5 Replace println in `GameRepositoryImpl.kt` (4 occurrences)
- [ ] 0.2.6 Replace println in `LoadInitialDataImpl.kt` (8 occurrences)
- [ ] 0.2.7 Replace println in `AppModule.kt` (7 occurrences - HTTP logging)
- [ ] 0.2.8 Replace println in `BooksRepositoryImpl.kt` (2 occurrences)
- [ ] 0.2.9 Replace println in `SearchRepositoryImpl.kt` (2 occurrences)
- [ ] 0.2.10 Replace println in `IGDBAuthManager.kt` (3 occurrences)
- [ ] 0.2.11 Replace println in `LoginViewModel.kt` (6 occurrences)
- [ ] 0.2.12 Replace println in `BookComponents.kt` (1 occurrence)
- [ ] 0.2.13 Replace println in `SplashViewModel.kt` (1 occurrence)

- [ ] 0.2.14 Global search to verify NO println remains
  ```bash
  grep -r "println(" composeApp/src/commonMain --include="*.kt"
  # Should return 0 results
  ```

**Acceptance Criteria:**

- ✅ 0 println() statements in production code
- ✅ All logging uses Napier with appropriate levels
- ✅ Git diff shows 64+ lines changed

---

## Task 0.3: Extract Magic Numbers to Constants

**Impact:** MEDIUM | **Effort:** 1 hour | **Owner:** `___________`

### Subtasks:

- [ ] 0.3.1 Create `utils/Constants.kt`
  ```kotlin
  object CacheConstants {
      const val DEFAULT_TTL_MS = 2 * 60 * 1000L // 2 minutes
      const val MAX_PAGES_DEFAULT = 5
      const val MIN_REFRESH_INTERVAL_MS = 30_000L
  }
  
  object NetworkConstants {
      const val HTTP_TIMEOUT_MS = 30_000L
      const val RETRY_COUNT = 3
      const val MAX_RETRY_DELAY_MS = 5_000L
  }
  
  object UIConstants {
      const val ANIMATION_DURATION_MS = 300
      const val DEBOUNCE_MS = 500
  }
  ```

- [ ] 0.3.2 Replace magic numbers in `MovieRepositoryImpl.kt`
    - Line 36: `private val TTL = 2 * 60 * 1000L` → `CacheConstants.DEFAULT_TTL_MS`

- [ ] 0.3.3 Replace magic numbers in `TvShowRepositoryImpl.kt`
- [ ] 0.3.4 Replace magic numbers in `LoadInitialDataImpl.kt`
- [ ] 0.3.5 Update DI module to use constants where needed

**Acceptance Criteria:**

- ✅ Constants.kt file created with all categories
- ✅ No magic numbers in repository implementations
- ✅ All numeric literals have named constants

---

## Task 0.4: Add Error Handling to Critical Use Cases

**Impact:** HIGH | **Effort:** 4 hours | **Owner:** `___________`

### Subtasks:

- [ ] 0.4.1 Create Result wrapper or sealed class
    - File: `domain/models/Result.kt`
  ```kotlin
  sealed class Result<out T> {
      data class Success<T>(val data: T) : Result<T>()
      data class Error(val exception: Throwable, val message: String? = null) : Result<Nothing>()
      data object Loading : Result<Nothing>()
  }
  ```

- [ ] 0.4.2 Add error handling to `ToggleFavoriteUseCase`
  ```kotlin
  suspend operator fun invoke(item: FavoriteItem): Result<Unit> {
      return try {
          // Existing logic
          Result.Success(Unit)
      } catch (e: Exception) {
          Napier.e("Error toggling favorite", e)
          Result.Error(e, "Failed to toggle favorite")
      }
  }
  ```

- [ ] 0.4.3 Add error handling to `ToggleMovieWatchedUseCase`
- [ ] 0.4.4 Add error handling to `RefreshBooksUseCase`
- [ ] 0.4.5 Add error handling to `GetInitialDataUseCase`
- [ ] 0.4.6 Update ViewModels to handle Result types

**Acceptance Criteria:**

- ✅ Result type created and documented
- ✅ 4 critical use cases have error handling
- ✅ No unhandled exceptions in use case layer

---

## Task 0.5: Create Base Test Classes

**Impact:** MEDIUM | **Effort:** 1.5 hours | **Owner:** `___________`

### Subtasks:

- [ ] 0.5.1 Create `ViewModelTest.kt` base class
    - File: `composeApp/src/commonTest/kotlin/org/lanzadera/proyectos/base/ViewModelTest.kt`
  ```kotlin
  @OptIn(ExperimentalCoroutinesApi::class)
  abstract class ViewModelTest {
      protected lateinit var testDispatcher: TestDispatcher
      
      @BeforeTest
      fun setupBase() {
          testDispatcher = StandardTestDispatcher()
          Dispatchers.setMain(testDispatcher)
      }
      
      @AfterTest
      fun tearDownBase() {
          Dispatchers.resetMain()
      }
  }
  ```

- [ ] 0.5.2 Create `RepositoryTest.kt` base class
- [ ] 0.5.3 Create `UseCaseTest.kt` base class
- [ ] 0.5.4 Create test utilities: `TestData.kt`
  ```kotlin
  object TestData {
      val testMovie = Movie(id = 1, title = "Test Movie", ...)
      val testTvShow = TvShow(id = 1, name = "Test Show", ...)
      // etc.
  }
  ```

**Acceptance Criteria:**

- ✅ 3 base test classes created
- ✅ TestData object with sample data
- ✅ Documentation on how to use base classes

---

## Task 0.6: Add Tests for 3 Critical Use Cases

**Impact:** HIGH | **Effort:** 3 hours | **Owner:** `___________`

### Subtasks:

- [ ] 0.6.1 Test `ToggleMovieWatchedUseCase`
    - File: `composeApp/src/commonTest/.../domain/usecase/movies/ToggleMovieWatchedUseCaseTest.kt`
    - Tests:
        - `invoke marks movie as watched`
        - `invoke unmarks watched movie`
        - `invoke handles repository error`

- [ ] 0.6.2 Test `RefreshBooksUseCase`
    - Tests:
        - `invoke refreshes books successfully`
        - `invoke handles network error`
        - `invoke respects force parameter`

- [ ] 0.6.3 Test `GetInitialDataUseCase`
    - Tests:
        - `invoke loads all data sources`
        - `invoke handles partial failures`
        - `invoke caches data correctly`

**Acceptance Criteria:**

- ✅ 3 use cases have comprehensive tests
- ✅ Tests use fakes, not mocks
- ✅ All tests pass

---

## Task 0.7: Setup Test Coverage Reporting

**Impact:** MEDIUM | **Effort:** 1 hour | **Owner:** `___________`

### Subtasks:

- [ ] 0.7.1 Add Kover plugin to `build.gradle.kts`
  ```kotlin
  plugins {
      id("org.jetbrains.kotlinx.kover") version "0.7.5"
  }
  ```

- [ ] 0.7.2 Configure Kover
  ```kotlin
  kover {
      reports {
          filters {
              excludes {
                  classes("*BuildConfig*", "*_Factory", "*_Impl")
              }
          }
      }
  }
  ```

- [ ] 0.7.3 Generate initial coverage report
  ```bash
  ./gradlew koverHtmlReport
  ```

- [ ] 0.7.4 Document coverage commands in README

**Acceptance Criteria:**

- ✅ Kover plugin configured
- ✅ Coverage report generated
- ✅ Baseline coverage documented (~5%)

---

## Task 0.8: Document Testing Strategy

**Impact:** LOW | **Effort:** 1 hour | **Owner:** `___________`

### Subtasks:

- [ ] 0.8.1 Create `TESTING.md` in project root
    - Testing philosophy
    - Test structure guidelines
    - How to run tests
    - How to write tests

- [ ] 0.8.2 Add testing examples to `COPILOT.md`
- [ ] 0.8.3 Create PR template with test checklist

**Acceptance Criteria:**

- ✅ TESTING.md document created
- ✅ Examples documented
- ✅ Team reviewed and approved

---

# 🏗️ PHASE 1: ARCHITECTURE FIXES (Weeks 1-2)

**Priority:** P0 - CRITICAL  
**Goal:** Fix architectural violations, reach 100/100 in Architecture  
**Estimated Time:** 34-38 hours (added 6h for Task 1.1.5)

## Task 1.1: Create Missing Use Cases for ViewModels

**Impact:** HIGH | **Effort:** 4 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.1.1 Create `ObserveWatchedMoviesUseCase`
    - File: `domain/usecase/movies/ObserveWatchedMoviesUseCase.kt`
  ```kotlin
  class ObserveWatchedMoviesUseCase(
      private val repository: WatchedMoviesRepository
  ) {
      operator fun invoke(): Flow<List<WatchedMovie>> {
          return repository.observeAllWatchedMovies()
      }
  }
  ```

- [ ] 1.1.2 Create `ObserveWatchedEpisodesUseCase`
    - File: `domain/usecase/episodes/ObserveWatchedEpisodesUseCase.kt`

- [ ] 1.1.3 Create `GetFavoriteDetailsUseCase`
    - File: `domain/usecase/favorites/GetFavoriteDetailsUseCase.kt`

- [ ] 1.1.4 Create `GetMovieDetailsUseCase`
    - File: `domain/usecase/movies/GetMovieDetailsUseCase.kt`

- [ ] 1.1.5 Create `GetTvShowDetailsUseCase`
    - File: `domain/usecase/tvshows/GetTvShowDetailsUseCase.kt`

- [ ] 1.1.6 Register all new use cases in `AppModule.kt`

**Acceptance Criteria:**

- ✅ 5 new use cases created
- ✅ All registered in DI
- ✅ Each has kdoc documentation

---

## Task 1.2: Refactor HomeViewModel - Remove Repository Injections

**Impact:** HIGH | **Effort:** 3 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.2.1 Replace `watchedEpisodesRepository` with `ObserveWatchedEpisodesUseCase`
    - Remove: `private val watchedEpisodesRepository: WatchedEpisodesRepository`
    - Add: `private val observeWatchedEpisodesUseCase: ObserveWatchedEpisodesUseCase`
    - Update all usages

- [ ] 1.2.2 Replace `favoriteDetailsRepository` with `GetFavoriteDetailsUseCase`
- [ ] 1.2.3 Replace `watchedMoviesRepository` with `ObserveWatchedMoviesUseCase`
- [ ] 1.2.4 Update all method calls to use use cases
- [ ] 1.2.5 Update DI configuration
- [ ] 1.2.6 Verify app compiles and runs

**Acceptance Criteria:**

- ✅ HomeViewModel only injects use cases, no repositories
- ✅ All functionality works as before
- ✅ Architecture violation resolved

---

## Task 1.3: Refactor MovieDetailViewModel

**Impact:** HIGH | **Effort:** 2 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.3.1 Replace `movieRepository` with `GetMovieDetailsUseCase`
- [ ] 1.3.2 Replace `watchedMoviesRepository` with use case
- [ ] 1.3.3 Update method implementations
- [ ] 1.3.4 Test movie detail screen functionality

**Acceptance Criteria:**

- ✅ MovieDetailViewModel only injects use cases
- ✅ Movie details load correctly
- ✅ Watch toggle works

---

## Task 1.4: Refactor SearchViewModel

**Impact:** MEDIUM | **Effort:** 1.5 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.4.1 Replace `searchRepository` with use cases
- [ ] 1.4.2 Create `SearchMoviesUseCase`, `SearchTvShowsUseCase` if needed
- [ ] 1.4.3 Update search logic
- [ ] 1.4.4 Test search functionality

**Acceptance Criteria:**

- ✅ SearchViewModel only injects use cases
- ✅ Search works for all content types

---

## Task 1.5: Split HomeViewModel - Phase 1 (Analysis & Design)

**Impact:** HIGH | **Effort:** 3 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.5.1 Analyze HomeViewModel responsibilities (577 lines)
    - Document all state flows
    - Document all methods
    - Identify coupling points

- [ ] 1.5.2 Design refactoring strategy
    - Option A: 5 separate ViewModels
    - Option B: Manager classes with single HomeViewModel
    - Option C: Hybrid approach
    - **Decision:** Document chosen approach with rationale

- [ ] 1.5.3 Create refactoring plan document
    - Break down into subtasks
    - Identify risks
    - Plan migration strategy

- [ ] 1.5.4 Review plan with team

**Acceptance Criteria:**

- ✅ Refactoring plan documented
- ✅ Approach chosen and justified
- ✅ Team buy-in achieved

---

## Task 1.6: Split HomeViewModel - Phase 2 (Implementation)

**Impact:** HIGH | **Effort:** 8 hours | **Owner:** `___________`

### Subtasks (if Option A - 5 ViewModels chosen):

- [ ] 1.6.1 Create `FavoritesTabViewModel`
    - Move favorites-related state
    - Move favorites-related methods
    - Test favorites tab independently

- [ ] 1.6.2 Create `BooksTabViewModel`
    - Move books-related state
    - Move books refresh logic

- [ ] 1.6.3 Create `FilmsTabViewModel`
- [ ] 1.6.4 Create `SeriesTabViewModel`
- [ ] 1.6.5 Create `GamesTabViewModel`

- [ ] 1.6.6 Update `HomeView.kt` to use new ViewModels
  ```kotlin
  val favoritesVM: FavoritesTabViewModel = koinViewModel()
  val booksVM: BooksTabViewModel = koinViewModel()
  // etc.
  ```

- [ ] 1.6.7 Update `AppModule.kt` to register all ViewModels
- [ ] 1.6.8 Remove old `HomeViewModel` (gradually)
- [ ] 1.6.9 Test all tabs work independently
- [ ] 1.6.10 Test tab switching

**Acceptance Criteria:**

- ✅ HomeViewModel split into smaller components
- ✅ Each component < 150 lines
- ✅ All tabs work correctly
- ✅ No functionality broken

---

## Task 1.7: Refactor ToggleFavoriteUseCase - Reduce Dependencies

**Impact:** MEDIUM | **Effort:** 3 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.7.1 Analyze current dependencies (5 repositories)
- [ ] 1.7.2 Option A: Create `FavoriteManager` facade
  ```kotlin
  class FavoriteManager(
      private val favoritesRepository: FavoritesRepository,
      private val favoriteDetailsRepository: FavoriteDetailsRepository,
      private val watchedEpisodesRepository: WatchedEpisodesRepository
  ) {
      suspend fun toggleTvShow(id: String, details: TvShow)
      suspend fun toggleMovie(id: String, details: Movie)
  }
  ```

- [ ] 1.7.3 Option B: Split into 3 use cases
    - `ToggleTvShowFavoriteUseCase`
    - `ToggleMovieFavoriteUseCase`
    - `ToggleBookFavoriteUseCase`

- [ ] 1.7.4 Implement chosen solution
- [ ] 1.7.5 Update call sites
- [ ] 1.7.6 Update tests

**Acceptance Criteria:**

- ✅ ToggleFavoriteUseCase has ≤ 3 dependencies OR is split
- ✅ Tests updated and passing
- ✅ Functionality unchanged

---

## Task 1.8: Create DTOs for Domain Models - Planning

**Impact:** HIGH | **Effort:** 2 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.8.1 Audit all domain models with @Serializable
    - `Movie.kt`, `TvShow.kt`, `Book.kt`, `Game.kt`
    - List all ~30 models

- [ ] 1.8.2 Design DTO structure
  ```
  data/
  ├── dto/
  │   ├── movie/
  │   │   ├── MovieDto.kt
  │   │   └── MovieDetailsDto.kt
  │   ├── tvshow/
  │   └── book/
  └── mapper/
      ├── MovieMapper.kt
      └── TvShowMapper.kt
  ```

- [ ] 1.8.3 Create migration plan
    - Phase 1: Movies (5 models)
    - Phase 2: TV Shows (8 models)
    - Phase 3: Books (3 models)
    - Phase 4: Games (5 models)
    - Phase 5: Other (9 models)

- [ ] 1.8.4 Document plan in `ARCHITECTURE.md`

**Acceptance Criteria:**

- ✅ All @Serializable models identified
- ✅ Migration plan created
- ✅ Plan reviewed and approved

---

## Task 1.9: Create DTOs - Movie Models

**Impact:** HIGH | **Effort:** 3 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.9.1 Create `data/dto/movie/MovieDto.kt`
  ```kotlin
  @Serializable
  data class MovieDto(
      val adult: Boolean? = null,
      @SerialName("backdrop_path") val backdropPath: String? = null,
      @SerialName("genre_ids") val genreIds: List<Int>? = null,
      // All fields from Movie with @SerialName annotations
  )
  ```

- [ ] 1.9.2 Create pure `domain/models/movie/Movie.kt`
  ```kotlin
  data class Movie(
      val adult: Boolean?,
      val backdropPath: String?,
      val genreIds: List<Int>?,
      // Clean domain model, NO annotations
  )
  ```

- [ ] 1.9.3 Create `data/mapper/MovieMapper.kt`
  ```kotlin
  fun MovieDto.toDomain(): Movie = Movie(
      adult = adult,
      backdropPath = backdropPath,
      genreIds = genreIds,
      // All mappings
  )
  
  fun Movie.toDto(): MovieDto = MovieDto(...)
  ```

- [ ] 1.9.4 Update `MovieRepositoryImpl` to use DTOs
    - Parse JSON to `MovieDto`
    - Map to `Movie` before returning

- [ ] 1.9.5 Update tests
- [ ] 1.9.6 Verify movies still load correctly

**Acceptance Criteria:**

- ✅ Movie DTOs created
- ✅ Domain Movie is annotation-free
- ✅ Mapper functions work
- ✅ App functionality unchanged

---

## Task 1.10-1.13: Create DTOs for Other Models

**Impact:** HIGH | **Effort:** 9 hours total | **Owner:** `___________`

### Task 1.10: TV Show Models (3 hours)

- [ ] Create `TvShowDto.kt`, `SeasonDto.kt`, `EpisodeDto.kt`
- [ ] Update domain models
- [ ] Create mappers
- [ ] Update `TvShowRepositoryImpl`

### Task 1.11: Book Models (2 hours)

- [ ] Create `BookDto.kt`
- [ ] Update domain model
- [ ] Create mapper
- [ ] Update `BooksRepositoryImpl`

### Task 1.12: Game Models (2 hours)

- [ ] Create `GameDto.kt`
- [ ] Update domain model
- [ ] Create mapper
- [ ] Update `GameRepositoryImpl`

### Task 1.13: Other Models (2 hours)

- [ ] Collection, Genre, Credits, etc.
- [ ] Create DTOs
- [ ] Update mappers

**Acceptance Criteria:**

- ✅ All domain models are annotation-free
- ✅ All DTOs in data layer
- ✅ All mappers tested
- ✅ 100% functionality preserved

---

## Task 1.14: Verify Repository Interfaces

**Impact:** MEDIUM | **Effort:** 1 hour | **Owner:** `___________`

### Subtasks:

- [ ] 1.14.1 List all repository implementations
- [ ] 1.14.2 Verify each has a domain interface
- [ ] 1.14.3 Check FavoriteDetailsRepository interface exists
- [ ] 1.14.4 Create missing interfaces if needed
- [ ] 1.14.5 Ensure all interfaces in `domain/repository/`

**Acceptance Criteria:**

- ✅ 10 repository interfaces confirmed
- ✅ All implementations reference interfaces
- ✅ No direct implementation injection

---

## Task 1.15: Document Architecture Decisions

**Impact:** LOW | **Effort:** 2 hours | **Owner:** `___________`

### Subtasks:

- [ ] 1.15.1 Create `docs/architecture/` folder
- [ ] 1.15.2 Write ADR-001: Clean Architecture
- [ ] 1.15.3 Write ADR-002: Use Case Layer
- [ ] 1.15.4 Write ADR-003: Repository Pattern
- [ ] 1.15.5 Write ADR-004: DTO vs Domain Models
- [ ] 1.15.6 Create architecture diagrams

**Acceptance Criteria:**

- ✅ 4 ADRs documented
- ✅ Architecture diagrams created
- ✅ Team reviewed

---

# 💎 PHASE 2: CODE QUALITY (Week 2)

**Priority:** P1 - HIGH  
**Goal:** Reach 100/100 in Code Quality  
**Estimated Time:** 14-16 hours

## Task 2.1: Add Error Handling to Remaining Use Cases

**Impact:** HIGH | **Effort:** 6 hours | **Owner:** `___________`

### Subtasks (12 use cases):

- [ ] 2.1.1 `RefreshBooksUseCase`
- [ ] 2.1.2 `ObserveWatchedEpisodesUseCase`
- [ ] 2.1.3 `ToggleEpisodeWatchedUseCase`
- [ ] 2.1.4 `RefreshGamesUseCase`
- [ ] 2.1.5 `SearchMoviesUseCase`
- [ ] 2.1.6 `SearchTvShowsUseCase`
- [ ] 2.1.7 `SearchBooksUseCase`
- [ ] 2.1.8 `RefreshTvShowsUseCase`
- [ ] 2.1.9 `GetTvShowDetailsUseCase`
- [ ] 2.1.10 `GetMovieDetailsUseCase`
- [ ] 2.1.11 `ObserveFavoritesUseCase` (update)
- [ ] 2.1.12 `SyncFavoritesUseCase` (update)

**Template for each:**

```kotlin
suspend operator fun invoke(...): Result<T> {
    return try {
        val result = // logic
        Result.Success(result)
    } catch (e: CancellationException) {
        throw e // Don't catch cancellation
    } catch (e: Exception) {
        Napier.e("Error in ${this::class.simpleName}", e)
        Result.Error(e, e.message)
    }
}
```

**Acceptance Criteria:**

- ✅ All 15 use cases have error handling
- ✅ CancellationException properly propagated
- ✅ All errors logged

---

## Task 2.2: Add Error Handling to Repositories

**Impact:** MEDIUM | **Effort:** 4 hours | **Owner:** `___________`

### Subtasks:

- [ ] 2.2.1 Add try-catch to `MovieRepositoryImpl` methods
- [ ] 2.2.2 Add try-catch to `TvShowRepositoryImpl` methods
- [ ] 2.2.3 Add try-catch to `BooksRepositoryImpl` methods
- [ ] 2.2.4 Add try-catch to `GameRepositoryImpl` methods
- [ ] 2.2.5 Add try-catch to `SearchRepositoryImpl` methods
- [ ] 2.2.6 Log all errors appropriately

**Acceptance Criteria:**

- ✅ Network failures don't crash app
- ✅ All errors logged with context
- ✅ Graceful degradation implemented

---

## Task 2.3: Apply .asStateFlow() Consistently

**Impact:** LOW | **Effort:** 1 hour | **Owner:** `___________`

### Subtasks:

- [ ] 2.3.1 Update all repositories to use `.asStateFlow()`
  ```kotlin
  private val _movies = MutableStateFlow<List<Movie>>(emptyList())
  override val moviesFlow: StateFlow<List<Movie>> = _movies.asStateFlow()
  ```
- [ ] 2.3.2 Update ViewModels if needed
- [ ] 2.3.3 Document pattern in style guide

**Acceptance Criteria:**

- ✅ All StateFlow exposed via .asStateFlow()
- ✅ Consistent pattern across codebase

---

## Task 2.4: Add KDoc to Public APIs

**Impact:** MEDIUM | **Effort:** 3 hours | **Owner:** `___________`

### Subtasks:

- [ ] 2.4.1 Add KDoc to all repository interfaces
  ```kotlin
  /**
   * Repository for managing movie data.
   * 
   * Provides access to movies from TMDB API with local caching.
   */
  interface MovieRepository { ... }
  ```

- [ ] 2.4.2 Add KDoc to all use cases
- [ ] 2.4.3 Add KDoc to domain models
- [ ] 2.4.4 Add KDoc to ViewModels
- [ ] 2.4.5 Generate KDoc HTML with Dokka

**Acceptance Criteria:**

- ✅ All public APIs documented
- ✅ KDoc follows Kotlin conventions
- ✅ HTML documentation generated

---

## Task 2.5: Code Review & Cleanup

**Impact:** LOW | **Effort:** 2 hours | **Owner:** `___________`

### Subtasks:

- [ ] 2.5.1 Run ktlint
  ```bash
  ./gradlew ktlintCheck
  ```
- [ ] 2.5.2 Fix all linting issues
- [ ] 2.5.3 Remove unused imports
- [ ] 2.5.4 Remove commented code
- [ ] 2.5.5 Format all files consistently

**Acceptance Criteria:**

- ✅ ktlint passes with 0 warnings
- ✅ No commented code blocks
- ✅ Consistent formatting

---

[... Continue with similar detail for all remaining phases ...]

