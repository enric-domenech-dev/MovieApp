# ADR-001: Clean Architecture

**Date:** December 7, 2025  
**Status:** ✅ Accepted  
**Context:** Task 1.15 - Architecture Documentation

---

## Context

The MovieApp is a Kotlin Multiplatform Compose application that manages movies, TV shows, books, and games. We need a scalable architecture that:
- Works across multiple platforms (Android, iOS, Desktop)
- Separates concerns clearly
- Is testable and maintainable
- Follows industry best practices

## Decision

We adopt **Clean Architecture** (by Robert C. Martin) with 3 main layers:

```
┌─────────────────────────────────────────┐
│           UI Layer (Compose)            │
│  - Screens, ViewModels, UI Models       │
└─────────────────┬───────────────────────┘
                  │ depends on
┌─────────────────▼───────────────────────┐
│          Domain Layer (Pure)            │
│  - Models, Repository Interfaces,       │
│    Use Cases                             │
└─────────────────┬───────────────────────┘
                  │ implements
┌─────────────────▼───────────────────────┐
│         Data Layer (Platform)           │
│  - Repository Implementations,          │
│    Data Sources, DTOs, Mappers          │
└─────────────────────────────────────────┘
```

### Layer Rules

#### Domain Layer (`domain/`)
- **Pure Kotlin** - No platform dependencies
- **No framework annotations** - No `@Serializable`, `@Entity`, etc.
- **Defines contracts** - Repository interfaces, use cases
- **Can only import:**
  - Kotlin stdlib
  - kotlinx-coroutines
  - kotlinx-datetime

#### Data Layer (`data/`)
- **Implements domain contracts**
- **Uses DTOs** for serialization (with `@Serializable`)
- **Maps DTOs** to domain models
- **Platform-specific code** via `expect/actual`
- **Can import:**
  - Domain layer (interfaces & models)
  - Framework libraries (Ktor, Room, etc.)

#### UI Layer (`ui/`)
- **Uses ViewModels** to manage state
- **Uses UI Models** (not domain models)
- **Injects use cases** (not repositories)
- **Can import:**
  - Compose UI framework
  - Koin DI
  - **Cannot import domain.models or domain.repository**

## Consequences

### Positive
✅ **Testability**: Easy to mock/fake dependencies  
✅ **Maintainability**: Clear separation of concerns  
✅ **Flexibility**: Easy to swap implementations  
✅ **Platform Independence**: Domain logic works everywhere  
✅ **Team Scalability**: Clear boundaries for parallel work

### Negative
⚠️ **More Files**: Each feature requires multiple files (model, DTO, mapper, repository, use case)  
⚠️ **Boilerplate**: Mapping between layers adds code  
⚠️ **Learning Curve**: Requires understanding of Clean Architecture

### Risks Mitigated
- ❌ UI directly using domain models → Use UI models
- ❌ Domain depending on frameworks → Pure Kotlin only
- ❌ God classes → Single Responsibility per layer

## Examples

### ✅ CORRECT Flow

```kotlin
// 1. Domain Model (domain/models/Movie.kt)
data class Movie(val id: Int, val title: String)

// 2. Repository Interface (domain/repository/MovieRepository.kt)
interface MovieRepository {
    suspend fun getMovies(): Result<List<Movie>>
}

// 3. Use Case (domain/usecase/GetMoviesUseCase.kt)
class GetMoviesUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke() = repo.getMovies()
}

// 4. DTO (data/dto/MovieDto.kt)
@Serializable
data class MovieDto(val id: Int, val title: String)

// 5. Mapper (data/mapper/MovieMapper.kt)
fun MovieDto.toDomain() = Movie(id = id, title = title)

// 6. Repository Implementation (data/repository/MovieRepositoryImpl.kt)
class MovieRepositoryImpl(
    private val api: HttpClient
) : MovieRepository {
    override suspend fun getMovies(): Result<List<Movie>> {
        val dtos = api.get<List<MovieDto>>("movies")
        return Result.Success(dtos.map { it.toDomain() })
    }
}

// 7. UI Model (ui/models/MovieUI.kt)
data class MovieUI(val id: Int, val title: String)

// 8. UI Mapper (ui/mapper/MovieMapper.kt)
fun Movie.toUI() = MovieUI(id = id, title = title)

// 9. ViewModel (ui/viewmodel/MoviesViewModel.kt)
class MoviesViewModel(
    private val useCase: GetMoviesUseCase
) : ViewModel() {
    val movies: StateFlow<List<MovieUI>> = useCase()
        .map { result -> result.data.map { it.toUI() } }
        .stateIn(...)
}

// 10. UI (ui/screens/MoviesScreen.kt)
@Composable
fun MoviesScreen(vm: MoviesViewModel = koinViewModel()) {
    val movies = vm.movies.collectAsState()
    LazyColumn {
        items(movies.value) { movie -> 
            Text(movie.title) // Uses MovieUI
        }
    }
}
```

### ❌ WRONG - Architecture Violations

```kotlin
// ❌ Domain model with @Serializable
@Serializable // WRONG - Domain can't depend on serialization
data class Movie(val id: Int)

// ❌ UI importing domain models
import org.lanzadera.proyectos.domain.models.Movie // WRONG

@Composable
fun MoviesScreen(movies: List<Movie>) // WRONG - Use MovieUI

// ❌ ViewModel injecting repository
class MoviesViewModel(
    private val repo: MovieRepository // WRONG - Use GetMoviesUseCase
) : ViewModel()

// ❌ Domain depending on platform
import androidx.room.Entity // WRONG - Domain can't depend on Room

@Entity
data class Movie(...) // WRONG - Put @Entity on DTO in data layer
```

## Compliance Verification

### How to verify Clean Architecture is followed:

```bash
# 1. Domain layer has no framework imports
grep -r "import io.ktor" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain
grep -r "import androidx" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain
grep -r "@Serializable" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/domain
# All should return 0 results

# 2. UI layer doesn't import domain models
grep -r "import org.lanzadera.proyectos.domain.models" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/ui
grep -r "import org.lanzadera.proyectos.domain.repository" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/ui
# Should return 0 results (except NavigationStore - being fixed in Task 1.16)

# 3. ViewModels only inject use cases
grep -r "Repository" composeApp/src/commonMain/kotlin/org/lanzadera/proyectos/ui/viewmodel
# Should only show UseCase injections, not Repository injections
```

## Current Status (Dec 7, 2025)

✅ **Domain Layer:** 100% clean - 0 framework dependencies  
✅ **Data Layer:** 100% compliant - DTOs with mappers  
✅ **Repository Pattern:** 10/10 interfaces defined  
✅ **Use Case Pattern:** 15+ use cases implemented  
⚠️ **UI Layer:** 95% compliant - NavigationStore needs refactoring (Task 1.16)

## Related ADRs

- ADR-002: Use Case Layer
- ADR-003: Repository Pattern
- ADR-004: DTO vs Domain Models

## References

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kotlin Multiplatform Best Practices](https://kotlinlang.org/docs/multiplatform.html)
