# HomeViewModel Refactoring Plan (Task 1.5)

**Created:** December 6, 2025  
**Status:** Analysis Complete - Ready for Implementation  
**Estimated Effort:** 8 hours (Phase 2 implementation)

---

## 1. CURRENT STATE ANALYSIS

### File Statistics
- **Lines of Code:** 638 lines
- **Constructor Parameters:** 9 dependencies (8 use cases + 1 optional)
- **State Flows:** 60+ StateFlow properties
- **Public Methods:** 4 methods
- **Private Methods:** 2 methods

### Dependencies Injected
1. ✅ `GetInitialDataUseCase` - Movies data loading
2. ✅ `RefreshBooksUseCase?` - Books refresh (optional)
3. ✅ `RefreshTvShowsUseCase?` - TV shows refresh (optional)
4. ✅ `RefreshGamesUseCase?` - Games refresh (optional)
5. ✅ `ObserveFavoritesUseCase` - Favorites observation
6. ✅ `ToggleFavoriteUseCase` - Toggle favorite action
7. ✅ `ObserveAllWatchedEpisodesUseCase` - Watched episodes tracking
8. ✅ `GetFavoriteDetailsUseCase` - Favorite details retrieval
9. ✅ `ObserveWatchedMoviesUseCase` - Watched movies tracking

### Responsibilities by Tab

#### 1. FAVORITES Tab (Following Content)
**Lines:** ~200 lines  
**StateFlows:** 8 flows
- `favorites` - List of all favorites
- `allWatchedEpisodes` - All watched episodes
- `seriesWithUnwatchedEpisodes` - TV shows with next episode
- `finishedSeries` - Completed TV shows
- `upcomingFavoriteMovies` - Upcoming favorite movies
- `moviesWithReleaseInfo` - Movies with release tracking
- `watchedMoviesWithInfo` - Watched movies
- `favoritesWithInfo` - Combined favorites with metadata

**Methods:**
- `findNextUnwatchedEpisode()` - Business logic for next episode detection
- `toggleFavorite()` - Toggle favorite status

**Dependencies:**
- `ObserveFavoritesUseCase`
- `ToggleFavoriteUseCase`
- `ObserveAllWatchedEpisodesUseCase`
- `GetFavoriteDetailsUseCase`
- `ObserveWatchedMoviesUseCase`

#### 2. BOOKS Tab
**Lines:** ~80 lines  
**StateFlows:** 9 flows
- `books` - All books
- `fictionBooks` - Fiction category
- `scienceBooks` - Science category
- `historyBooks` - History category
- `biographyBooks` - Biography category
- `businessBooks` - Business category
- `technologyBooks` - Technology category
- `selfHelpBooks` - Self-help category
- `recentBooks` - Recent books

**Refresh Logic:** Lines 470-500 (30 lines)
- Lazy loading when tab selected
- 8 parallel refresh operations

**Dependencies:**
- `RefreshBooksUseCase?`

#### 3. FILMS Tab
**Lines:** ~120 lines  
**StateFlows:** 9 flows
- `movies` - Discover movies
- `trendingWeek` - Trending this week
- `trendingDay` - Trending today
- `popular` - Popular movies
- `topRated` - Top rated
- `upcoming` - Upcoming releases
- `discover` - Discover feed
- `hero` - Hero section
- `inCinemasToday` - Now in cinemas

**Refresh Logic:** Handled by `GetInitialDataUseCase` (already loaded)

**Dependencies:**
- `GetInitialDataUseCase`

#### 4. SERIES Tab
**Lines:** ~120 lines  
**StateFlows:** 11 flows
- `tvShows` - All TV shows
- `popularTvShows` - Popular
- `topRatedTvShows` - Top rated
- `onAirTvShows` - On air
- `trendingTvShows` - Trending
- `airingTodayTvShows` - Airing today
- `trendingTvShowsWeek` - Trending this week
- `airingTodayAndTrendingTvShows` - Combined (derived)
- `recommendedTvShows` - Recommendations (derived)
- `upcomingTvShows` - Upcoming (derived)

**Refresh Logic:** Lines 503-531 (29 lines) + 437-467 (31 lines for FAVORITES)
- Lazy loading when tab selected
- 7 parallel refresh operations
- **DUPLICATION:** Same refresh logic in 2 places (FAVORITES + SERIES tabs)

**Dependencies:**
- `RefreshTvShowsUseCase?`

#### 5. GAMES Tab
**Lines:** ~60 lines  
**StateFlows:** 5 flows
- `games` - All games
- `popularGames` - Popular
- `topRatedGames` - Top rated
- `upcomingGames` - Upcoming
- `trendingGames` - Trending

**Refresh Logic:** Lines 535-556 (22 lines)
- Lazy loading when tab selected
- Single refresh operation

**Dependencies:**
- `RefreshGamesUseCase?`

#### 6. Shared State
**Lines:** ~30 lines  
**StateFlows:** 4 flows
- `refreshing` - Loading indicator
- `error` - Error message
- `selectedTab` - Current tab
- `uiState` - Combined UI state

**Methods:**
- `selectTab()` - Tab selection with lazy loading
- `refresh()` - Manual refresh trigger
- `refreshIfNeeded()` - Conditional refresh

---

## 2. PROBLEMS IDENTIFIED

### 2.1 Violations of SOLID Principles

#### Single Responsibility Principle (SRP) ❌
- HomeViewModel manages **5 different screens** worth of data
- Each tab has distinct responsibilities and dependencies
- 638 lines is 4x the recommended size (150 lines)

#### Open/Closed Principle (OCP) ⚠️
- Adding a new tab requires modifying HomeViewModel
- `selectTab()` has 5 if-statements (one per tab)

#### Dependency Inversion Principle (DIP) ⚠️
- 9 constructor parameters is excessive (recommended: ≤ 5)
- Many dependencies are optional (nullable)

### 2.2 Code Duplication
- TV shows refresh logic duplicated in 2 places:
  - Lines 437-467 (FAVORITES tab)
  - Lines 503-531 (SERIES tab)
- Same 7 parallel operations in both

### 2.3 Testability Issues
- Impossible to test individual tabs in isolation
- Mock setup requires all 9 dependencies
- Test would be 200+ lines just for setup

### 2.4 Performance Issues
- All 60+ StateFlows initialized on app start
- Memory overhead even if user never visits certain tabs
- FAVORITES tab logic runs even if never viewed

### 2.5 Maintainability Issues
- Hard to understand which flows belong to which tab
- Difficult to find specific tab logic (scattered across 638 lines)
- Risky to modify (high chance of breaking other tabs)

---

## 3. REFACTORING OPTIONS

### Option A: 5 Separate ViewModels ⭐ **RECOMMENDED**

**Structure:**
```
ui/screens/home/
├── HomeViewModel.kt (coordinator - 80 lines)
├── tabs/
│   ├── FavoritesTabViewModel.kt (200 lines)
│   ├── BooksTabViewModel.kt (100 lines)
│   ├── FilmsTabViewModel.kt (120 lines)
│   ├── SeriesTabViewModel.kt (120 lines)
│   └── GamesTabViewModel.kt (80 lines)
```

**Pros:**
- ✅ Each ViewModel has single responsibility
- ✅ Easy to test independently
- ✅ Lazy initialization (memory efficient)
- ✅ Clear separation of concerns
- ✅ Follows Android best practices
- ✅ Easy to add new tabs (OCP compliant)

**Cons:**
- ⚠️ More files (but better organized)
- ⚠️ Need to pass ViewModels to HomeView

**Code Example:**
```kotlin
// HomeView.kt
@Composable
fun HomeView(
    homeViewModel: HomeViewModel = koinViewModel(),
    favoritesViewModel: FavoritesTabViewModel = koinViewModel(),
    booksViewModel: BooksTabViewModel = koinViewModel(),
    filmsViewModel: FilmsTabViewModel = koinViewModel(),
    seriesViewModel: SeriesTabViewModel = koinViewModel(),
    gamesViewModel: GamesTabViewModel = koinViewModel()
) {
    val selectedTab by homeViewModel.selectedTab.collectAsState()
    
    when (selectedTab) {
        HomeTab.FAVORITES -> FavoritesTab(favoritesViewModel)
        HomeTab.BOOKS -> BooksTab(booksViewModel)
        HomeTab.FILMS -> FilmsTab(filmsViewModel)
        HomeTab.SERIES -> SeriesTab(seriesViewModel)
        HomeTab.GAMES -> GamesTab(gamesViewModel)
    }
}
```

### Option B: Manager Classes + Single ViewModel

**Structure:**
```
ui/screens/home/
├── HomeViewModel.kt (coordinator - 150 lines)
├── managers/
│   ├── FavoritesManager.kt
│   ├── BooksManager.kt
│   ├── FilmsManager.kt
│   ├── SeriesManager.kt
│   └── GamesManager.kt
```

**Pros:**
- ✅ Extracts logic into testable units
- ✅ Single ViewModel (familiar pattern)

**Cons:**
- ❌ Manager classes are NOT standard Android pattern
- ❌ Still 9 dependencies in HomeViewModel
- ❌ Managers need viewModelScope from VM (tight coupling)
- ❌ No lazy initialization benefit
- ❌ Harder to test (need ViewModel for coroutine scope)

### Option C: Hybrid (Shared State + Tab ViewModels)

**Structure:**
```
ui/screens/home/
├── HomeViewModel.kt (shared state only - 100 lines)
├── tabs/
│   ├── FavoritesTabViewModel.kt
│   ├── BooksTabViewModel.kt
│   └── ... (3 more)
```

**Pros:**
- ✅ Balance between single and multiple VMs
- ✅ Shared state centralized

**Cons:**
- ⚠️ More complex communication between VMs
- ⚠️ Potential for race conditions

---

## 4. DECISION: OPTION A - 5 SEPARATE VIEWMODELS

### Rationale

1. **Best Practices Alignment**
   - Follows Google's Android Architecture Guide
   - Each screen/tab = 1 ViewModel (SRP)
   - Recommended by Compose documentation

2. **Testability**
   - Each VM can be tested in isolation
   - Mock 2-3 dependencies instead of 9
   - Test suite will be 80% smaller

3. **Performance**
   - Lazy initialization saves memory
   - StateFlows only created when tab viewed
   - Faster app startup

4. **Maintainability**
   - Easy to find code for specific tab
   - Changes to one tab don't risk others
   - New developers can understand quickly

5. **Scalability**
   - Easy to add new tabs (just add new VM)
   - Easy to add features to specific tab
   - No risk of HomeViewModel growing to 1000+ lines

### Risks & Mitigation

**Risk 1:** Shared state duplication (e.g., favorites used in multiple tabs)  
**Mitigation:** Use same use case instances (Koin singleton), StateFlow shares data

**Risk 2:** More files to manage  
**Mitigation:** Clear folder structure (`tabs/`), better than 638-line file

**Risk 3:** Communication between tabs  
**Mitigation:** Use shared use cases (repository pattern already handles this)

---

## 5. IMPLEMENTATION PLAN (Task 1.6)

### Phase 1: Create Tab ViewModels (4 hours)

#### Step 1.1: Create FavoritesTabViewModel (1 hour)
**File:** `ui/screens/home/tabs/FavoritesTabViewModel.kt`

**Responsibilities:**
- Following content tracking
- Next episode detection
- Release date tracking
- Toggle favorites

**StateFlows to migrate:**
- `favorites`
- `allWatchedEpisodes`
- `seriesWithUnwatchedEpisodes`
- `finishedSeries`
- `upcomingFavoriteMovies`
- `moviesWithReleaseInfo`
- `watchedMoviesWithInfo`
- `favoritesWithInfo`

**Methods to migrate:**
- `findNextUnwatchedEpisode()`
- `toggleFavorite()`

**Dependencies:**
- `ObserveFavoritesUseCase`
- `ToggleFavoriteUseCase`
- `ObserveAllWatchedEpisodesUseCase`
- `GetFavoriteDetailsUseCase`
- `ObserveWatchedMoviesUseCase`

#### Step 1.2: Create BooksTabViewModel (45 min)

**StateFlows to migrate:** All 9 book flows  
**Dependencies:** `RefreshBooksUseCase?`

#### Step 1.3: Create FilmsTabViewModel (30 min)

**StateFlows to migrate:** All 9 movie flows  
**Dependencies:** `GetInitialDataUseCase`

#### Step 1.4: Create SeriesTabViewModel (1 hour)

**StateFlows to migrate:** All 11 TV show flows  
**Dependencies:** `RefreshTvShowsUseCase?`

#### Step 1.5: Create GamesTabViewModel (30 min)

**StateFlows to migrate:** All 5 game flows  
**Dependencies:** `RefreshGamesUseCase?`

### Phase 2: Update HomeViewModel (1 hour)

Simplify to only manage tab selection (~80 lines)

### Phase 3: Update DI (15 min)

Register 5 new ViewModels in AppModule.kt

### Phase 4: Update HomeView (1 hour)

Inject all 6 ViewModels, use when() for tab switching

### Phase 5: Testing (1.5 hours)

Create tests for all 6 ViewModels (~21 tests total)

### Phase 6: Cleanup (30 min)

Documentation and verification

---

## 6. BENEFITS SUMMARY

### Before Refactoring
- 📁 1 file: 638 lines
- 🔧 9 dependencies
- ⚡ 60+ StateFlows initialized on startup
- 🧪 Untestable (too complex)
- 📈 Tech debt: HIGH

### After Refactoring
- 📁 6 files: avg 120 lines each
- 🔧 2-5 dependencies per ViewModel
- ⚡ StateFlows lazy-loaded per tab
- 🧪 21 unit tests (easy to write)
- 📈 Tech debt: LOW

### Metrics Improvement
| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Lines per file | 638 | 80-200 | ⬇️ 68% |
| Constructor params | 9 | 0-5 | ⬇️ 56% |
| StateFlows on startup | 60+ | ~10 | ⬇️ 83% |
| Test complexity | HIGH | LOW | ⬇️ 80% |
| Memory usage | HIGH | LOW | ⬇️ 50%* |

*Estimated based on lazy initialization

---

## 7. NEXT STEPS

1. ✅ **Review this plan** with team
2. ⏳ **Start Phase 1** (Task 1.6): Create tab ViewModels
3. ⏳ **Complete Phase 2-6**: Update HomeViewModel, DI, HomeView, tests
4. ⏳ **Update TODO_LIST.md**: Mark Task 1.5 complete, start Task 1.6

**Estimated Total Time:** 8 hours  
**Recommended Sessions:** 2-3 sessions (3-4 hours each)

---

**Document Status:** ✅ COMPLETE - Ready for Implementation
