# ADR-002: Use Case Layer

**Date:** December 7, 2025  
**Status:** ✅ Accepted  
**Context:** Task 1.15 - Architecture Documentation

---

## Context

In Clean Architecture, the **Use Case layer** (also called Interactors or Application Business Rules) encapsulates application-specific business logic. We need to define how use cases should be structured in the MovieApp.

## Decision

Every application operation that requires business logic or orchestrates multiple repositories should have a dedicated **Use Case**.

### Use Case Principles

#### 1. Single Responsibility Principle (SRP)
Each use case does **ONE thing** and has **ONE public method**: `operator fun invoke()`

```kotlin
// ✅ CORRECT - One responsibility
class ToggleMovieFavoriteUseCase(
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) {
        // Single responsibility: Toggle movie favorite status
    }
}

// ❌ WRONG - Multiple responsibilities
class FavoriteUseCase(
    private val repository: FavoriteDetailsRepository
) {
    suspend fun toggleMovie(id: Int) { ... }
    suspend fun toggleTvShow(id: Int) { ... } // Different responsibility
    suspend fun getAll(): List<Favorite> { ... } // Different responsibility
}
```

#### 2. Use Cases vs Repositories

| Concept | Use Case | Repository |
|---------|----------|------------|
| **Purpose** | Business logic | Data access |
| **Orchestration** | Can use multiple repos | Single data source |
| **Naming** | Verb (Action) | Noun (Resource) |
| **Example** | `ToggleMovieFavoriteUseCase` | `FavoriteDetailsRepository` |
| **Can depend on** | Repositories, other use cases | Data sources only |

#### 3. Naming Convention

Use cases follow: `<Verb><Noun>UseCase`

Examples:
- `ToggleFavoriteUseCase` - Action on favorites
- `GetMovieDetailsUseCase` - Fetch movie details
- `ObserveWatchedMoviesUseCase` - Observe watched movies
- `RefreshBooksUseCase` - Refresh books data
- `SearchMoviesUseCase` - Search for movies

#### 4. When to Create a Use Case

✅ **Create a use case when:**
- Business logic is needed (validation, transformation, calculations)
- Multiple repositories need to be orchestrated
- The operation is reused in multiple ViewModels
- You want to keep ViewModels thin and testable

❌ **Don't create a use case when:**
- Simple CRUD with no logic (e.g., direct repository call)
- The operation is ViewModel-specific (e.g., UI state management)

### Use Case Structure

```kotlin
/**
 * Use case for [brief description].
 * 
 * @param repository The [RepositoryName] to fetch/update data
 */
class MyUseCase(
    private val repository: MyRepository
) {
    /**
     * Executes the use case.
     * 
     * @param param Description of parameter
     * @return Result of the operation
     */
    suspend operator fun invoke(param: String): Result<Data> {
        return try {
            // Business logic here
            val result = repository.getData(param)
            Result.Success(result)
        } catch (e: CancellationException) {
            throw e // Never catch cancellation
        } catch (e: Exception) {
            Logger.e("Error in MyUseCase", throwable = e)
            Result.Error(e, e.message)
        }
    }
}
```

## Examples

### Example 1: Simple Use Case (Delegate to Repository)

```kotlin
// When: No business logic needed, just data access
class ObserveWatchedMoviesUseCase(
    private val repository: WatchedMoviesRepository
) {
    operator fun invoke(): Flow<List<WatchedMovie>> {
        return repository.observeAllWatchedMovies()
    }
}
```

**Why?** Even though it's simple, it decouples ViewModel from repository and makes testing easier.

### Example 2: Use Case with Business Logic

```kotlin
class ToggleMovieFavoriteUseCase(
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Unit> {
        return try {
            // Business logic: Check if movie is already favorite
            val existingFavorite = favoriteDetailsRepository.getFavoriteMovie(movieId.toString())
            
            if (existingFavorite != null) {
                // Remove from favorites
                favoriteDetailsRepository.removeFavoriteMovie(movieId.toString())
                Logger.d("Movie removed from favorites: $movieId")
            } else {
                // Add to favorites
                val movie = movieRepository.getMovieDetails(movieId)
                favoriteDetailsRepository.saveFavoriteMovie(movie)
                Logger.d("Movie added to favorites: $movieId")
            }
            
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling movie favorite", throwable = e)
            Result.Error(e, "Failed to toggle favorite")
        }
    }
}
```

### Example 3: Use Case Orchestrating Multiple Repositories

```kotlin
class GetFavoriteDetailsUseCase(
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(favoriteId: String, type: FavoriteType): Result<Any> {
        return try {
            when (type) {
                FavoriteType.MOVIE -> {
                    val favorite = favoriteDetailsRepository.getFavoriteMovie(favoriteId)
                    if (favorite == null) {
                        // Fetch from remote if not in local favorites
                        val movie = movieRepository.getMovieDetails(favoriteId.toInt())
                        Result.Success(movie)
                    } else {
                        Result.Success(favorite)
                    }
                }
                FavoriteType.TVSHOW -> {
                    val favorite = favoriteDetailsRepository.getFavoriteTvShow(favoriteId)
                    if (favorite == null) {
                        val tvShow = tvShowRepository.getTvShowDetails(favoriteId.toInt())
                        Result.Success(tvShow)
                    } else {
                        Result.Success(favorite)
                    }
                }
                else -> Result.Error(IllegalArgumentException("Unsupported type"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error getting favorite details", throwable = e)
            Result.Error(e)
        }
    }
}
```

## Use Case Categories in MovieApp

### 1. Favorites Management (5 use cases)
- `ObserveFavoritesUseCase` - Observe all favorites
- `ToggleMovieFavoriteUseCase` - Toggle movie favorite
- `ToggleTvShowFavoriteUseCase` - Toggle TV show favorite
- `ToggleBookFavoriteUseCase` - Toggle book favorite
- `ToggleGameFavoriteUseCase` - Toggle game favorite
- `GetFavoriteDetailsUseCase` - Get favorite with details

### 2. Movies (4 use cases)
- `GetMovieDetailsUseCase` - Get movie details
- `ObserveWatchedMoviesUseCase` - Observe watched movies
- `ToggleMovieWatchedUseCase` - Toggle watched status
- `SearchMoviesUseCase` - Search movies

### 3. TV Shows (4 use cases)
- `GetTvShowDetailsUseCase` - Get TV show details
- `ObserveAllWatchedEpisodesUseCase` - Observe watched episodes
- `ToggleEpisodeWatchedUseCase` - Toggle episode watched
- `RefreshTvShowsUseCase` - Refresh TV shows data
- `SearchTvShowsUseCase` - Search TV shows

### 4. Books (2 use cases)
- `RefreshBooksUseCase` - Refresh books data
- `SearchBooksUseCase` - Search books

### 5. Games (1 use case)
- `RefreshGamesUseCase` - Refresh games data

### 6. Initial Data (1 use case)
- `GetInitialDataUseCase` - Load initial app data

**Total:** 15+ use cases

## Testing Use Cases

```kotlin
class ToggleMovieFavoriteUseCaseTest : UseCaseTest() {
    private lateinit var useCase: ToggleMovieFavoriteUseCase
    private lateinit var fakeDetailsRepo: FakeFavoriteDetailsRepository
    private lateinit var fakeMovieRepo: FakeMovieRepository
    
    @BeforeTest
    fun setup() {
        fakeDetailsRepo = FakeFavoriteDetailsRepository()
        fakeMovieRepo = FakeMovieRepository()
        useCase = ToggleMovieFavoriteUseCase(fakeDetailsRepo, fakeMovieRepo)
    }
    
    @Test
    fun `should add movie to favorites when not favorited`() = runTest {
        // Given
        val movieId = 1
        fakeMovieRepo.addMovie(TestData.testMovie)
        
        // When
        val result = useCase(movieId)
        
        // Then
        assertTrue(result.isSuccess)
        assertNotNull(fakeDetailsRepo.getFavoriteMovie(movieId.toString()))
    }
    
    @Test
    fun `should remove movie from favorites when already favorited`() = runTest {
        // Given
        val movieId = 1
        fakeDetailsRepo.saveFavoriteMovie(TestData.testMovie)
        
        // When
        val result = useCase(movieId)
        
        // Then
        assertTrue(result.isSuccess)
        assertNull(fakeDetailsRepo.getFavoriteMovie(movieId.toString()))
    }
}
```

## Consequences

### Positive
✅ **Single Responsibility**: Each use case has one clear purpose  
✅ **Testability**: Easy to test business logic in isolation  
✅ **Reusability**: Use cases can be shared across ViewModels  
✅ **Thin ViewModels**: ViewModels focus on UI state, not business logic  
✅ **Dependency Inversion**: ViewModels depend on use cases, not repositories

### Negative
⚠️ **More Classes**: Each operation requires a new class  
⚠️ **Potential Over-Engineering**: Simple CRUD might not need use cases  
⚠️ **Boilerplate**: Operator invoke pattern adds syntax

### Mitigated Risks
- ❌ Fat ViewModels → Use cases keep ViewModels thin
- ❌ Untestable business logic → Use cases are easily testable
- ❌ Code duplication → Reusable use cases

## Current Status (Dec 7, 2025)

✅ **15+ use cases** implemented following SRP  
✅ **All use cases** have single `operator fun invoke()` method  
✅ **ViewModels** inject use cases, not repositories (after Task 1.2-1.4)  
✅ **Error handling** with Result types in critical use cases

## Related ADRs

- ADR-001: Clean Architecture
- ADR-003: Repository Pattern

## References

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Single Responsibility Principle](https://en.wikipedia.org/wiki/Single-responsibility_principle)
