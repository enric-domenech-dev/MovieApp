# DTO Migration Plan - Domain Model Refactoring

**Task:** 1.8 - Create DTOs for Domain Models  
**Created:** December 7, 2025  
**Status:** Planning Complete

---

## 📋 Executive Summary

**Problem:** Domain models currently have `@Serializable` annotations, violating Clean Architecture principles. Domain layer should be pure Kotlin with no framework dependencies.

**Solution:** Create DTOs in the data layer for serialization, keeping domain models clean.

**Scope:**
- **Total Models with @Serializable:** 38 classes
- **Files to create:** ~40 DTOs + ~15 mappers
- **Estimated effort:** 12-15 hours

---

## 🎯 Goals

1. ✅ Remove all `@Serializable` and `@SerialName` annotations from domain models
2. ✅ Create DTOs in `data/dto/` matching API response structures
3. ✅ Create mappers in `data/mapper/` to convert DTO ↔ Domain
4. ✅ Update repositories to use DTOs for network/database operations
5. ✅ Maintain 100% functionality (no behavior changes)

---

## 📊 Current State - Model Inventory

### Movies (5 models)
| Model | @Serializable Count | Location | Notes |
|-------|---------------------|----------|-------|
| `Movie` | 2 | domain/models/movie/ | Main + Collection nested |
| `MovieResponse` | 1 | domain/models/movie/ | API response wrapper |
| `Collection` | 1 | (nested in Movie.kt) | Belongs to collection |
| `MovieWithReleaseInfo` | 0 | domain/models/movie/ | Pure domain (no change) |

### TV Shows (14 models)
| Model | @Serializable Count | Location | Notes |
|-------|---------------------|----------|-------|
| `TvShow` | 14 | domain/models/tvshow/ | Main + 13 nested models |
| `TvShowResponse` | 1 | domain/models/tvshow/ | API response wrapper |
| `Genre` | ✓ | (nested) | Shared with movies |
| `ProductionCompany` | ✓ | (nested) | Shared with movies |
| `ProductionCountry` | ✓ | (nested) | Shared with movies |
| `SpokenLanguage` | ✓ | (nested) | Shared with movies |
| `Season` | ✓ | (nested) | Has episodes array |
| `Episode` | ✓ | (nested) | Episode details |
| `AggregateCast` | ✓ | (nested) | Cast with roles |
| `CastRole` | ✓ | (nested) | Character info |
| `AggregateCrew` | ✓ | (nested) | Crew with jobs |
| `CrewJob` | ✓ | (nested) | Job info |
| `AggregateCredits` | ✓ | (nested) | Credits wrapper |
| `CreatedBy` | ✓ | (nested) | Creator info |
| `Network` | ✓ | (nested) | TV network |
| `TvShowWithNextEpisode` | 0 | domain/models/tvshow/ | Pure domain (no change) |

### Books (5 models)
| Model | @Serializable Count | Location | Notes |
|-------|---------------------|----------|-------|
| `Book` | 1 | domain/models/book/ | Main model |
| `GoogleBooksResponse` | 4 | domain/models/book/ | Response + 3 nested |
| `VolumeItem` | ✓ | (nested) | Book item wrapper |
| `VolumeInfo` | ✓ | (nested) | Book metadata |
| `ImageLinks` | ✓ | (nested) | Cover images |

### Games (13 models)
| Model | @Serializable Count | Location | Notes |
|-------|---------------------|----------|-------|
| `Game` | 13 | domain/models/game/ | Main + 12 nested |
| `Genre` | ✓ | (nested) | Game genre |
| `Platform` | ✓ | (nested) | Gaming platform |
| `ReleaseDate` | ✓ | (nested) | Release info |
| `Cover` | ✓ | (nested) | Has getImageUrl() method |
| `Screenshot` | ✓ | (nested) | Has getImageUrl() method |
| `Company` | ✓ | (nested) | Developer/Publisher |
| `Keyword` | ✓ | (nested) | Game tags |
| `InvolvedCompany` | ✓ | (nested) | Company role |
| `Artwork` | ✓ | (nested) | Has getImageUrl() method |
| `Website` | ✓ | (nested) | Official sites |
| `GameEngine` | ✓ | (nested) | Engine used |
| `GameMode` | ✓ | (nested) | Single/Multi player |

### Collections (2 models)
| Model | @Serializable Count | Location | Notes |
|-------|---------------------|----------|-------|
| `Collection` | 1 | domain/models/collection/ | Separate file |
| `CollectionResponse` | 1 | domain/models/collection/ | API response |

### Other (3 models)
| Model | @Serializable Count | Location | Notes |
|-------|---------------------|----------|-------|
| `User` | 1 | domain/models/user/ | User profile |
| `WatchedMovie` | 0 | domain/models/ | Pure domain (Room entity?) |
| `WatchedEpisode` | 0 | domain/models/ | Pure domain (Room entity?) |
| `FavoriteItem` | 0 | domain/models/favorite/ | Pure domain (Room entity?) |
| `FavoriteItemWithInfo` | 0 | domain/models/favorite/ | Pure domain |
| `FavoriteType` | 0 | domain/models/favorite/ | Pure domain enum |
| `Result` | 0 | domain/models/ | Pure domain sealed class |

**Total:** 38 classes with `@Serializable` annotations

---

## 🏗️ Target Architecture

```
data/
├── dto/
│   ├── movie/
│   │   ├── MovieDto.kt             # API response
│   │   ├── MovieResponseDto.kt     # Paginated response
│   │   └── CollectionDto.kt        # Belongs to collection
│   ├── tvshow/
│   │   ├── TvShowDto.kt            # Main model
│   │   ├── TvShowResponseDto.kt    # Paginated response
│   │   ├── SeasonDto.kt
│   │   ├── EpisodeDto.kt
│   │   ├── AggregateCreditsDto.kt  # With nested Cast/Crew
│   │   ├── CreatedByDto.kt
│   │   └── NetworkDto.kt
│   ├── book/
│   │   ├── GoogleBooksResponseDto.kt
│   │   ├── VolumeItemDto.kt
│   │   ├── VolumeInfoDto.kt
│   │   └── ImageLinksDto.kt
│   ├── game/
│   │   ├── GameDto.kt              # Main model
│   │   ├── CoverDto.kt             # Keep getImageUrl()
│   │   ├── ScreenshotDto.kt        # Keep getImageUrl()
│   │   ├── ArtworkDto.kt           # Keep getImageUrl()
│   │   └── ...                     # + 9 more nested
│   ├── collection/
│   │   ├── CollectionDto.kt
│   │   └── CollectionResponseDto.kt
│   ├── user/
│   │   └── UserDto.kt
│   └── common/                     # Shared DTOs
│       ├── GenreDto.kt             # Used by movies & TV
│       ├── ProductionCompanyDto.kt
│       ├── ProductionCountryDto.kt
│       └── SpokenLanguageDto.kt
└── mapper/
    ├── MovieMapper.kt              # Movie ↔ MovieDto
    ├── TvShowMapper.kt             # TvShow ↔ TvShowDto
    ├── BookMapper.kt               # Book ↔ GoogleBooksDto
    ├── GameMapper.kt               # Game ↔ GameDto
    ├── CollectionMapper.kt
    ├── UserMapper.kt
    └── CommonMapper.kt             # Shared models

domain/
└── models/
    ├── movie/
    │   ├── Movie.kt                # Clean - no annotations
    │   ├── MovieResponse.kt        # Keep as domain wrapper
    │   └── MovieWithReleaseInfo.kt # No changes
    ├── tvshow/
    │   ├── TvShow.kt               # Clean - no annotations
    │   └── ...                     # All nested models clean
    └── ...
```

---

## 📝 Migration Strategy

### Phase 1: Movies (3 hours)
**Priority:** P0 - Start here, most common model

**Files to create:**
1. `data/dto/movie/MovieDto.kt` - Movie with all @Serializable + @SerialName
2. `data/dto/movie/MovieResponseDto.kt` - Paginated response
3. `data/dto/movie/CollectionDto.kt` - Belongs to collection
4. `data/mapper/MovieMapper.kt` - Bidirectional mapping functions

**Files to update:**
1. `domain/models/movie/Movie.kt` - Remove annotations, keep logic
2. `data/repository/MovieRepositoryImpl.kt` - Use DTOs for HTTP calls

**Pattern:**
```kotlin
// DTO (data layer)
@Serializable
data class MovieDto(
    val adult: Boolean? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    // ... all fields with API naming
)

// Domain model (pure Kotlin)
data class Movie(
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: List<Int>?,
    // ... clean domain names
) {
    val voteAverage: String  // Keep computed properties
        get() = voteAverageDouble?.let { (it * 10).toInt() / 10.0 }?.toString() ?: "N/A"
}

// Mapper (data layer)
fun MovieDto.toDomain(): Movie = Movie(
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    // ... map all fields
)

// Repository usage
class MovieRepositoryImpl(private val client: HttpClient) {
    suspend fun getMovie(id: Int): Movie {
        val dto = client.get("movie/$id").body<MovieDto>()
        return dto.toDomain()  // Convert to domain
    }
}
```

**Acceptance Criteria:**
- ✅ MovieDto created with all serialization
- ✅ Movie.kt has no @Serializable or @SerialName
- ✅ MovieMapper tested
- ✅ MovieRepositoryImpl uses DTOs
- ✅ All movie screens work correctly
- ✅ Build successful

---

### Phase 2: TV Shows (4 hours)
**Priority:** P0 - Most complex model (14 nested classes)

**Files to create:**
1. `data/dto/tvshow/TvShowDto.kt` - Main model
2. `data/dto/tvshow/TvShowResponseDto.kt`
3. `data/dto/tvshow/SeasonDto.kt` - With episodes array
4. `data/dto/tvshow/EpisodeDto.kt`
5. `data/dto/tvshow/AggregateCreditsDto.kt` - Cast/Crew
6. `data/dto/tvshow/AggregateCastDto.kt`
7. `data/dto/tvshow/CastRoleDto.kt`
8. `data/dto/tvshow/AggregateCrewDto.kt`
9. `data/dto/tvshow/CrewJobDto.kt`
10. `data/dto/tvshow/CreatedByDto.kt`
11. `data/dto/tvshow/NetworkDto.kt`
12. `data/dto/common/GenreDto.kt` - Shared with movies
13. `data/dto/common/ProductionCompanyDto.kt`
14. `data/dto/common/ProductionCountryDto.kt`
15. `data/dto/common/SpokenLanguageDto.kt`
16. `data/mapper/TvShowMapper.kt` - All nested models
17. `data/mapper/CommonMapper.kt` - Shared models

**Files to update:**
1. `domain/models/tvshow/TvShow.kt` - Remove all annotations
2. `data/repository/TvShowRepositoryImpl.kt` - Use DTOs

**Acceptance Criteria:**
- ✅ All 14 TvShow DTOs created
- ✅ All domain models clean
- ✅ Mappers tested
- ✅ TV show screens work
- ✅ Build successful

---

### Phase 3: Books (1.5 hours)
**Priority:** P1 - Simple model, only 5 classes

**Files to create:**
1. `data/dto/book/GoogleBooksResponseDto.kt`
2. `data/dto/book/VolumeItemDto.kt`
3. `data/dto/book/VolumeInfoDto.kt`
4. `data/dto/book/ImageLinksDto.kt`
5. `data/mapper/BookMapper.kt`

**Files to update:**
1. `domain/models/book/GoogleBooksResponse.kt` - Remove annotations
2. `data/repository/BooksRepositoryImpl.kt` - Use DTOs

**Acceptance Criteria:**
- ✅ All book DTOs created
- ✅ Domain models clean
- ✅ Mapper tested
- ✅ Book screens work
- ✅ Build successful

---

### Phase 4: Games (3 hours)
**Priority:** P1 - Complex with 13 nested classes + image URL logic

**Files to create:**
1. `data/dto/game/GameDto.kt`
2. `data/dto/game/GenreDto.kt`
3. `data/dto/game/PlatformDto.kt`
4. `data/dto/game/ReleaseDateDto.kt`
5. `data/dto/game/CoverDto.kt` - **Keep getImageUrl() method**
6. `data/dto/game/ScreenshotDto.kt` - **Keep getImageUrl() method**
7. `data/dto/game/CompanyDto.kt`
8. `data/dto/game/KeywordDto.kt`
9. `data/dto/game/InvolvedCompanyDto.kt`
10. `data/dto/game/ArtworkDto.kt` - **Keep getImageUrl() method**
11. `data/dto/game/WebsiteDto.kt`
12. `data/dto/game/GameEngineDto.kt`
13. `data/dto/game/GameModeDto.kt`
14. `data/mapper/GameMapper.kt`

**Special consideration:** Image URL construction methods
```kotlin
// Option A: Keep in DTO (practical)
@Serializable
data class CoverDto(
    @SerialName("image_id") val imageId: String? = null,
    val url: String? = null
) {
    fun getImageUrl(): String {
        return if (!imageId.isNullOrEmpty()) {
            "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.jpg"
        } else url ?: ""
    }
}

// Option B: Move to domain (cleaner architecture)
data class Cover(
    val imageId: String?,
    val url: String?
) {
    val imageUrl: String
        get() = if (!imageId.isNullOrEmpty()) {
            "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.jpg"
        } else url ?: ""
}
```

**Recommendation:** Use Option B - Keep image logic in domain, DTOs are pure data.

**Files to update:**
1. `domain/models/game/Game.kt` - Remove annotations
2. `data/repository/GameRepositoryImpl.kt` - Use DTOs

**Acceptance Criteria:**
- ✅ All game DTOs created
- ✅ Image URL logic preserved
- ✅ Domain models clean
- ✅ Mapper tested
- ✅ Game screens work
- ✅ Game images display correctly
- ✅ Build successful

---

### Phase 5: Collections & Other (1.5 hours)
**Priority:** P2 - Small models

**Files to create:**
1. `data/dto/collection/CollectionDto.kt`
2. `data/dto/collection/CollectionResponseDto.kt`
3. `data/dto/user/UserDto.kt`
4. `data/mapper/CollectionMapper.kt`
5. `data/mapper/UserMapper.kt`

**Files to update:**
1. `domain/models/collection/Collection.kt` - Remove annotations
2. `domain/models/user/User.kt` - Remove annotations
3. Update any repositories that use these models

**Acceptance Criteria:**
- ✅ All remaining DTOs created
- ✅ All domain models annotation-free
- ✅ Mappers tested
- ✅ Build successful

---

### Phase 6: Verification & Documentation (1 hour)

**Tasks:**
1. Verify NO `@Serializable` in domain/models/
   ```bash
   grep -r "@Serializable" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain/models/
   # Should return 0 results
   ```

2. Verify NO `@SerialName` in domain/models/
   ```bash
   grep -r "@SerialName" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain/models/
   # Should return 0 results
   ```

3. Run full test suite
   ```bash
   ./gradlew :composeApp:allTests
   ```

4. Test all screens manually
   - Movies: List, Detail, Search
   - TV Shows: List, Detail, Episodes
   - Books: List, Detail
   - Games: List, Detail
   - Favorites: All content types

5. Update COPILOT.md with DTO pattern
6. Document mapper conventions
7. Update TODO_LIST.md progress

**Acceptance Criteria:**
- ✅ 0 serialization annotations in domain/
- ✅ All tests passing
- ✅ All screens functional
- ✅ No regressions
- ✅ Documentation updated

---

## 🎨 Coding Conventions

### DTO Naming
- DTOs end with `Dto` suffix: `MovieDto`, `TvShowDto`
- Response wrappers: `MovieResponseDto`, `TvShowResponseDto`
- Keep API field names: `@SerialName("backdrop_path") val backdropPath: String?`

### Mapper Naming
- Extension functions for clarity: `fun MovieDto.toDomain(): Movie`
- Bidirectional: `fun Movie.toDto(): MovieDto` (if needed)
- Nested mappers: `fun List<GenreDto>.toDomain(): List<Genre>`

### Domain Models
- Clean Kotlin: No annotations, no framework dependencies
- Keep computed properties: `val voteAverage: String get() = ...`
- Keep business logic: `toString()`, formatting methods
- Keep domain methods: `getImageUrl()` moves to domain as computed property

### File Organization
```
data/
├── dto/
│   ├── {feature}/         # One folder per feature
│   │   └── {Model}Dto.kt  # One DTO per file
│   └── common/            # Shared DTOs (Genre, etc.)
└── mapper/
    └── {Feature}Mapper.kt # One mapper per feature
```

---

## ⚠️ Risks & Mitigation

### Risk 1: Breaking Serialization
**Mitigation:** Test each phase independently, verify JSON deserialization works

### Risk 2: Forgotten Annotations
**Mitigation:** Use grep to verify before marking phase complete

### Risk 3: Image URLs Broken (Games)
**Mitigation:** Test game images specifically, keep `getImageUrl()` logic intact

### Risk 4: Mapper Bugs
**Mitigation:** Write unit tests for mappers, verify all fields mapped

### Risk 5: Repository Errors
**Mitigation:** Test each repository after updating, check error handling

---

## ✅ Acceptance Criteria (Full Task)

### Technical
- [ ] All domain models are annotation-free
- [ ] All DTOs in `data/dto/` with proper serialization
- [ ] All mappers in `data/mapper/` with tests
- [ ] All repositories use DTOs for network/DB operations
- [ ] 0 compilation errors
- [ ] All existing tests pass

### Functional
- [ ] Movies: List, detail, search work
- [ ] TV Shows: List, detail, episodes work
- [ ] Books: List, detail, search work
- [ ] Games: List, detail, images display
- [ ] Favorites: All content types work
- [ ] Watched tracking: Movies & episodes work

### Documentation
- [ ] COPILOT.md updated with DTO pattern
- [ ] Mapper conventions documented
- [ ] TODO_LIST.md marked complete

---

## 📅 Estimated Timeline

| Phase | Time | Cumulative |
|-------|------|------------|
| Phase 1: Movies | 3h | 3h |
| Phase 2: TV Shows | 4h | 7h |
| Phase 3: Books | 1.5h | 8.5h |
| Phase 4: Games | 3h | 11.5h |
| Phase 5: Collections & Other | 1.5h | 13h |
| Phase 6: Verification | 1h | 14h |
| **Total** | **14h** | - |

**Session breakdown:**
- Session 1: Phase 1 + 2 (7h) - Movies + TV Shows
- Session 2: Phase 3 + 4 (4.5h) - Books + Games
- Session 3: Phase 5 + 6 (2.5h) - Finish + Verify

---

## 🚀 Next Steps

1. **Review this plan** - Ensure all models are covered
2. **Approve approach** - Confirm DTO structure is acceptable
3. **Start Phase 1** - Begin with movies (most common)
4. **Update TODO_LIST.md** - Mark Task 1.8 as complete, start Task 1.9

---

**Plan Status:** ✅ COMPLETE - Ready for implementation  
**Next Task:** 1.9 - Implement Phase 1 (Movies)
