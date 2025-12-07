# ADR-003: Repository Pattern

**Date:** December 7, 2025  
**Status:** ✅ Accepted  
**Context:** Task 1.15 - Architecture Documentation

---

## Context

The MovieApp needs a consistent way to manage data access across multiple data sources (APIs, local database, cache). The Repository Pattern provides a clean abstraction over data sources.

## Decision

We implement the **Repository Pattern** with:
- **Interfaces in domain layer** (contracts)
- **Implementations in data layer** (concrete classes)
- **Multiple data sources** (remote API, local database, in-memory cache)

### Repository Structure

```
domain/repository/          # Interfaces (contracts)
├── MovieRepository.kt
├── TvShowRepository.kt
├── BooksRepository.kt
└── ...

data/repository/           # Implementations
├── MovieRepositoryImpl.kt
├── TvShowRepositoryImpl.kt
├── BooksRepositoryImpl.kt
└── ...

data/datasource/           # Platform-specific data sources
├── FavoriteDetailsRepositoryProvider.kt (expect)
├── FavoriteDetailsRepositoryProvider.android.kt (actual)
├── FavoriteDetailsRepositoryProvider.ios.kt (actual)
└── FavoriteDetailsRepositoryProvider.desktop.kt (actual)
```

## Repository Principles

### 1. Interface in Domain, Implementation in Data

✅ **CORRECT:**
```kotlin
// domain/repository/MovieRepository.kt
interface MovieRepository {
    val moviesFlow: StateFlow<List<Movie>>
    suspend fun refreshMovies(force: Boolean = false)
    suspend fun getMovieDetails(movieId: Int): Movie
}

// data/repository/MovieRepositoryImpl.kt
class MovieRepositoryImpl(
    private val apiClient: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : MovieRepository {
    // Implementation details
}
```

❌ **WRONG:**
```kotlin
// Domain layer with implementation
class MovieRepository(...) { // No interface - can't be mocked
    // Implementation
}
```

### 2. Dependency Inversion

ViewModels and use cases depend on **abstractions** (interfaces), not **concretions** (implementations).

```kotlin
// ✅ CORRECT
class MoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase // Use case
) : ViewModel()

class GetMoviesUseCase(
    private val repository: MovieRepository // Interface
) {
    suspend operator fun invoke() = repository.getMovies()
}

// ❌ WRONG
class MoviesViewModel(
    private val repository: MovieRepositoryImpl // Concrete class
) : ViewModel()
```

### 3. Single Data Source per Repository

Each repository manages **one type of data** (e.g., movies, TV shows, books).

```kotlin
// ✅ CORRECT - Single responsibility
interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieDetails(id: Int): Movie
}

interface TvShowRepository {
    suspend fun getTvShows(): List<TvShow>
    suspend fun getTvShowDetails(id: Int): TvShow
}

// ❌ WRONG - Multiple responsibilities
interface MediaRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getTvShows(): List<TvShow>
    suspend fun getBooks(): List<Book> // Too many responsibilities
}
```

### 4. Expose Flow/StateFlow for Reactive Data

Repositories expose `Flow<T>` or `StateFlow<T>` for reactive data streams.

```kotlin
interface MovieRepository {
    val moviesFlow: StateFlow<List<Movie>> // Reactive
    suspend fun refreshMovies(force: Boolean = false) // Action
}
```

## Repository Categories in MovieApp

### 1. Content Repositories (API-based)
Manage content from external APIs with caching.

| Repository | Data Source | Cache | Platform |
|------------|-------------|-------|----------|
| `MovieRepository` | TMDB API | In-memory | All |
| `TvShowRepository` | TMDB API | In-memory | All |
| `BooksRepository` | Google Books API | In-memory | All |
| `GameRepository` | IGDB API | In-memory | All |
| `SearchRepository` | TMDB + Google Books | None | All |

**Implementation Pattern:**
```kotlin
class MovieRepositoryImpl(
    private val apiClient: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _movies.asStateFlow()
    
    private var lastRefreshTime = 0L
    private val cacheTTL = Constants.DEFAULT_TTL_MS
    
    override suspend fun refreshMovies(force: Boolean) {
        if (!force && System.currentTimeMillis() - lastRefreshTime < cacheTTL) {
            return // Cache hit
        }
        
        val dtos = apiClient.get<MovieResponseDto>("discover/movie")
        val movies = dtos.results.map { it.toDomain() }
        _movies.value = movies
        lastRefreshTime = System.currentTimeMillis()
    }
}
```

### 2. User Data Repositories (Database-based)
Manage user-specific data with local persistence.

| Repository | Data Source | Persistence | Platform |
|------------|-------------|-------------|----------|
| `FavoritesRepository` | Local DB | Realm | All |
| `FavoriteDetailsRepository` | Local DB | Room (Android), Realm (iOS) | Platform-specific |
| `WatchedMoviesRepository` | Local DB | Room (Android) | Platform-specific |
| `WatchedEpisodesRepository` | Local DB | Room (Android) | Platform-specific |

**Implementation Pattern (Platform-Specific):**
```kotlin
// data/datasource/FavoriteDetailsRepositoryProvider.kt
expect fun createFavoriteDetailsRepository(): FavoriteDetailsRepository

// data/datasource/FavoriteDetailsRepositoryProvider.android.kt
actual fun createFavoriteDetailsRepository(): FavoriteDetailsRepository {
    val database = FavoritesDatabase.instance
    return FavoriteDetailsRepositoryImpl(
        tvShowDao = database.favoriteTvShowDao(),
        movieDao = database.favoriteMovieDao(),
        json = Json { ... }
    )
}
```

### 3. Initial Data Repository
Special repository for app initialization.

| Repository | Purpose | Data Source |
|------------|---------|-------------|
| `LoadInitialData` | Load all data on app start | Multiple APIs |

**Implementation Pattern:**
```kotlin
class LoadInitialDataImpl(
    private val client: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : LoadInitialData {
    override suspend fun refreshMovies(force: Boolean) {
        // Load movies in parallel
    }
    
    override suspend fun refreshAll() {
        coroutineScope {
            launch { refreshMovies() }
            launch { refreshTvShows() }
            // etc.
        }
    }
}
```

## Repository Responsibilities

### What Repositories Should Do
✅ **Data Access**: Fetch from API, database, cache  
✅ **Caching**: Implement TTL-based cache  
✅ **Data Mapping**: DTO → Domain model conversion  
✅ **Error Handling**: Catch network/database errors  
✅ **Reactive Streams**: Expose Flow/StateFlow for observation

### What Repositories Should NOT Do
❌ **Business Logic**: That belongs in use cases  
❌ **UI State Management**: That belongs in ViewModels  
❌ **Direct User Interaction**: That belongs in UI layer  
❌ **Complex Orchestration**: Use multiple use cases instead

## Testing Repositories

### Unit Tests with Fakes
```kotlin
class MovieRepositoryImplTest : RepositoryTest() {
    private lateinit var repository: MovieRepository
    private lateinit var mockClient: HttpClient
    
    @BeforeTest
    fun setup() {
        mockClient = createMockHttpClient()
        repository = MovieRepositoryImpl(mockClient, 5, json)
    }
    
    @Test
    fun `should fetch movies from API and cache them`() = runTest {
        // Given
        val expectedMovies = listOf(TestData.testMovie)
        
        // When
        repository.refreshMovies(force = true)
        
        // Then
        repository.moviesFlow.test {
            assertEquals(expectedMovies, awaitItem())
        }
    }
    
    @Test
    fun `should use cache when TTL not expired`() = runTest {
        // First fetch
        repository.refreshMovies(force = true)
        val firstFetchTime = System.currentTimeMillis()
        
        // Second fetch (should use cache)
        repository.refreshMovies(force = false)
        
        // Verify no second API call (cache hit)
    }
}
```

### Integration Tests (with Real Dependencies)
```kotlin
@Test
fun `should deserialize TMDB response correctly`() = runTest {
    val realJson = """{"results":[{"id":1,"title":"Test Movie"}]}"""
    val mockClient = MockEngine { request ->
        respond(
            content = realJson,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
    
    val repository = MovieRepositoryImpl(HttpClient(mockClient), 1, json)
    repository.refreshMovies(force = true)
    
    assertNotNull(repository.moviesFlow.value.firstOrNull())
}
```

## DI Registration

All repositories registered as **singletons** in `AppModule.kt`:

```kotlin
// Content repositories (API-based)
single<MovieRepository> { MovieRepositoryImpl(get(), 5, get()) }
single<TvShowRepository> { TvShowRepositoryImpl(get(), 5, get()) }
single<BooksRepository> { BooksRepositoryImpl(get(), get()) }
single<GameRepository> { GameRepositoryImpl(get(), get(named("igdbClient")), 5, get()) }
single<SearchRepository> { SearchRepositoryImpl(get(), get()) }

// User data repositories (DB-based)
single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
single<FavoriteDetailsRepository> { createFavoriteDetailsRepository() }
single<WatchedEpisodesRepository> { WatchedEpisodesRepositoryImpl(get()) }
single<WatchedMoviesRepository> { WatchedMoviesRepositoryImpl(createWatchedMoviesDataSource()) }

// Initial data
single<LoadInitialData> { LoadInitialDataImpl(get(), 5, get()) }
```

## Consequences

### Positive
✅ **Testability**: Interfaces can be mocked/faked easily  
✅ **Flexibility**: Easy to swap implementations (e.g., change DB from Room to Realm)  
✅ **Separation of Concerns**: Clear boundary between data and business logic  
✅ **Multiplatform Support**: Platform-specific implementations via expect/actual  
✅ **Caching Strategy**: Centralized cache management

### Negative
⚠️ **Indirection**: Extra layer between use cases and data sources  
⚠️ **Boilerplate**: Interface + implementation for each repository

### Mitigated Risks
- ❌ Tight coupling to data sources → Repository abstracts implementation
- ❌ Difficult to test → Interfaces enable fakes/mocks
- ❌ Platform-specific code in common → expect/actual pattern

## Current Status (Dec 7, 2025)

✅ **10 repository interfaces** defined in domain layer  
✅ **10 repository implementations** (9 common + 1 platform-specific)  
✅ **All registered in DI** with interface types  
✅ **0 direct implementation injections** in ViewModels or use cases  
✅ **Caching implemented** for API-based repositories

## Related ADRs

- ADR-001: Clean Architecture
- ADR-002: Use Case Layer
- ADR-004: DTO vs Domain Models

## References

- [Repository Pattern by Martin Fowler](https://martinfowler.com/eaaCatalog/repository.html)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
