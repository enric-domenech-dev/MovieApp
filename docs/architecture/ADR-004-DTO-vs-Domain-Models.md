# ADR-004: DTO vs Domain Models

**Date:** December 7, 2025  
**Status:** ✅ Accepted  
**Context:** Task 1.15 - Architecture Documentation

---

## Context

In Clean Architecture, the **domain layer** should be pure and framework-agnostic. However, serialization frameworks like `kotlinx.serialization` require annotations (`@Serializable`, `@SerialName`) on data classes. This creates a conflict: should domain models have serialization annotations, or should we create separate DTOs?

## Problem

**Initial Approach (Wrong):**
Domain models had `@Serializable` annotations, violating Clean Architecture.

```kotlin
// domain/models/Movie.kt
@Serializable // ❌ Domain depends on kotlinx.serialization
data class Movie(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("vote_average") val voteAverage: Double
)
```

**Problems:**
- ❌ Domain layer depends on serialization framework
- ❌ Can't change serialization library without changing domain
- ❌ Domain models coupled to API response structure
- ❌ Violates Dependency Inversion Principle

## Decision

We separate **DTOs** (Data Transfer Objects) from **Domain Models**:

- **DTOs** - In data layer, with `@Serializable` for network/database
- **Domain Models** - Pure Kotlin, no annotations
- **Mappers** - Convert between DTOs and domain models

### Structure

```
data/
├── dto/                    # DTOs with @Serializable
│   ├── movie/
│   │   ├── MovieDto.kt
│   │   ├── MovieResponseDto.kt
│   │   └── CollectionDto.kt
│   └── ...
│
├── mapper/                 # DTO ↔ Domain converters
│   ├── MovieMapper.kt
│   ├── TvShowMapper.kt
│   └── ...
│
└── repository/            # Use DTOs for serialization
    └── MovieRepositoryImpl.kt

domain/
└── models/                # Pure domain models
    ├── movie/
    │   ├── Movie.kt       # NO @Serializable
    │   └── Collection.kt
    └── ...
```

## Implementation

### 1. DTO (Data Layer)

DTOs match API/database structure with serialization annotations:

```kotlin
// data/dto/movie/MovieDto.kt
@Serializable
data class MovieDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("overview") val overview: String?
    // ... all API fields with @SerialName
)
```

### 2. Domain Model (Domain Layer)

Domain models are pure Kotlin with business-friendly names:

```kotlin
// domain/models/movie/Movie.kt
data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val voteAverageDouble: Double, // Business logic property
    val releaseDate: String?,
    val backdropPath: String?,
    val posterPath: String?,
    val overview: String?
) {
    // Domain logic
    val voteAverage: Float
        get() = voteAverageDouble.toFloat()
    
    val hasBackdrop: Boolean
        get() = !backdropPath.isNullOrEmpty()
}
```

### 3. Mapper (Data Layer)

Mappers convert between DTOs and domain models:

```kotlin
// data/mapper/MovieMapper.kt

// DTO → Domain
fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    originalTitle = originalTitle,
    voteAverageDouble = voteAverage,
    releaseDate = releaseDate,
    backdropPath = backdropPath,
    posterPath = posterPath,
    overview = overview
)

// Domain → DTO (for saving to database)
fun Movie.toDto(): MovieDto = MovieDto(
    id = id,
    title = title,
    originalTitle = originalTitle,
    voteAverage = voteAverageDouble,
    releaseDate = releaseDate,
    backdropPath = backdropPath,
    posterPath = posterPath,
    overview = overview
)

// List conversions
fun List<MovieDto>.toDomain(): List<Movie> = map { it.toDomain() }
fun List<Movie>.toDto(): List<MovieDto> = map { it.toDto() }
```

### 4. Repository Usage (Data Layer)

Repositories use DTOs for serialization, return domain models:

```kotlin
// data/repository/MovieRepositoryImpl.kt
class MovieRepositoryImpl(
    private val apiClient: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : MovieRepository {
    
    override suspend fun getMovieDetails(movieId: Int): Movie {
        // Deserialize API response to DTO
        val dto = apiClient.get<MovieDto>("movie/$movieId")
        
        // Convert DTO to domain model
        return dto.toDomain()
    }
    
    override suspend fun refreshMovies(force: Boolean) {
        // Deserialize list response
        val responseDto = apiClient.get<MovieResponseDto>("discover/movie")
        
        // Convert DTOs to domain models
        val movies = responseDto.results.map { it.toDomain() }
        
        _movies.value = movies
    }
}
```

## Examples

### Example 1: API Response → Domain

```kotlin
// 1. API returns JSON
val json = """
{
  "id": 550,
  "title": "Fight Club",
  "vote_average": 8.4
}
"""

// 2. Deserialize to DTO
val dto = Json.decodeFromString<MovieDto>(json)

// 3. Convert to domain model
val movie = dto.toDomain()

// 4. Use domain model in business logic
if (movie.voteAverageDouble > 8.0) {
    // High-rated movie logic
}
```

### Example 2: Saving to Database (Room)

```kotlin
// data/repository/FavoriteDetailsRepositoryImpl.kt
class FavoriteDetailsRepositoryImpl(
    private val movieDao: FavoriteMovieDao,
    private val json: Json
) : FavoriteDetailsRepository {
    
    override suspend fun saveFavoriteMovie(movie: Movie) {
        // Convert domain model to DTO for serialization
        val dto = movie.toDto()
        
        // Serialize DTO to JSON for Room
        val jsonString = json.encodeToString(dto)
        
        // Save to database
        movieDao.insert(FavoriteMovieEntity(
            id = movie.id.toString(),
            movieJson = jsonString
        ))
    }
    
    override suspend fun getFavoriteMovie(movieId: String): Movie? {
        val entity = movieDao.getById(movieId) ?: return null
        
        // Deserialize JSON to DTO
        val dto = json.decodeFromString<MovieDto>(entity.movieJson)
        
        // Convert DTO to domain model
        return dto.toDomain()
    }
}
```

### Example 3: Complex Nested DTOs

```kotlin
// TV Show with nested seasons and episodes
@Serializable
data class TvShowDto(
    val id: Int,
    val name: String,
    val seasons: List<SeasonDto>?
)

@Serializable
data class SeasonDto(
    @SerialName("season_number") val seasonNumber: Int,
    val episodes: List<EpisodeDto>?
)

@Serializable
data class EpisodeDto(
    @SerialName("episode_number") val episodeNumber: Int,
    val name: String
)

// Mapper handles nested conversions
fun TvShowDto.toDomain(): TvShow = TvShow(
    id = id,
    name = name,
    seasons = seasons?.map { it.toDomain() } // Recursive mapping
)

fun SeasonDto.toDomain(): Season = Season(
    seasonNumber = seasonNumber,
    episodes = episodes?.map { it.toDomain() }
)

fun EpisodeDto.toDomain(): Episode = Episode(
    episodeNumber = episodeNumber,
    name = name
)
```

## DTO Naming Conventions

| Type | Naming Pattern | Example |
|------|---------------|---------|
| **DTO** | `<Entity>Dto` | `MovieDto`, `TvShowDto` |
| **Response Wrapper** | `<Entity>ResponseDto` | `MovieResponseDto` |
| **Domain Model** | `<Entity>` | `Movie`, `TvShow` |
| **Mapper File** | `<Entity>Mapper.kt` | `MovieMapper.kt` |
| **Mapper Function** | `.toDomain()`, `.toDto()` | `movie.toDomain()` |

## Current Status (Dec 7, 2025)

### DTO Migration Complete ✅

| Feature | DTOs Created | Mappers Created | Domain Cleaned |
|---------|--------------|-----------------|----------------|
| **Movies** | 12 files | MovieMapper.kt | ✅ 0 @Serializable |
| **TV Shows** | 11 files | TvShowMapper.kt | ✅ 0 @Serializable |
| **Books** | 2 files | BookMapper.kt | ✅ 0 @Serializable |
| **Games** | 13 files | GameMapper.kt | ✅ 0 @Serializable |
| **Common** | 5 files | CommonMapper.kt | ✅ 0 @Serializable |
| **Other** | 2 files | CollectionMapper.kt, UserMapper.kt | ✅ 0 @Serializable |

**Total:** 23 DTO files, 9 mapper files, 0 @Serializable in domain layer

### Verification Commands

```bash
# Verify domain layer is clean (should return 0 results)
grep -r "@Serializable" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain
grep -r "@SerialName" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain
grep -r "import kotlinx.serialization" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain

# Count DTOs and mappers
find composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/data/dto -name "*.kt" | wc -l
find composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/data/mapper -name "*.kt" | wc -l
```

## Consequences

### Positive
✅ **Pure Domain Layer** - No framework dependencies  
✅ **Flexibility** - Easy to change serialization library  
✅ **Business Logic** - Domain models can have computed properties  
✅ **Testability** - Domain models easier to test (no serialization setup)  
✅ **API Independence** - Domain models not coupled to API structure

### Negative
⚠️ **More Files** - 2x files (DTO + Domain model for each entity)  
⚠️ **Boilerplate** - Mapper functions for every model  
⚠️ **Maintenance** - Changes require updating DTO, domain, and mapper

### Mitigated Risks
- ✅ **Serialization Bugs** - DTOs catch serialization issues early (see Task 1.10 bug)
- ✅ **Domain Pollution** - Domain stays clean and framework-agnostic
- ✅ **Breaking Changes** - API changes only affect DTOs, not domain

## Lessons Learned

### Bug Found in Task 1.10 (Dec 7, 2025)

**Problem:** When we removed `@Serializable` from `Season` domain model, we forgot to add `Season.toDto()` reverse mapper. This caused Room database serialization to fail silently.

**Root Cause:** Domain models were being serialized directly to JSON for Room storage.

**Solution:**
1. Added reverse mappers (`.toDto()`) for all domain models
2. Repositories now use DTOs for ALL serialization (API + Database)
3. Added integration tests to catch serialization bugs (Task 3.3)

**Lesson:** **Every DTO MUST have bidirectional mappers** (`toDomain()` and `toDto()`)

## Testing DTOs

### Serialization Tests (Required)

```kotlin
class MovieMapperTest {
    @Test
    fun `MovieDto can serialize and deserialize`() {
        val dto = MovieDto(id = 1, title = "Test", ...)
        
        // Round-trip test
        val json = Json.encodeToString(dto)
        val decoded = Json.decodeFromString<MovieDto>(json)
        
        assertEquals(dto, decoded)
    }
    
    @Test
    fun `MovieDto to Domain preserves all fields`() {
        val dto = MovieDto(id = 1, title = "Test", voteAverage = 8.5, ...)
        
        val domain = dto.toDomain()
        
        assertEquals(dto.id, domain.id)
        assertEquals(dto.title, domain.title)
        assertEquals(dto.voteAverage, domain.voteAverageDouble)
    }
    
    @Test
    fun `Domain to DTO round-trip preserves data`() {
        val domain = Movie(id = 1, title = "Test", voteAverageDouble = 8.5, ...)
        
        val dto = domain.toDto()
        val backToDomain = dto.toDomain()
        
        assertEquals(domain, backToDomain)
    }
}
```

## Related ADRs

- ADR-001: Clean Architecture
- ADR-002: Use Case Layer
- ADR-003: Repository Pattern

## References

- [DTO Pattern by Martin Fowler](https://martinfowler.com/eaaCatalog/dataTransferObject.html)
- [kotlinx.serialization Documentation](https://github.com/Kotlin/kotlinx.serialization)
- [Clean Architecture - Domain Layer](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
