# COPILOT.md

This file provides guidance to Copilot when working with code in this repository.

**📚 Required Reading (read these files at session start):**

1. [COPILOT.md](./COPILOT.md) - Architecture, build commands, and project structure
2. [copilot-instructions.md](./.github/copilot-instructions.md) - How to interact with Copilot in this project
3. [AUDIT_REPORT.md](./AUDIT_REPORT.md) - Comprehensive code audit with identified issues
4. [TODO_LIST.md](./TODO_LIST.md) - Detailed task list to reach 100/100 score

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

## Project Architecture

**Kotlin Multiplatform Compose** app targeting Android (primary), iOS, and Desktop. Follows Clean Architecture with MVVM
in the presentation layer.

### Source Structure

```
composeApp/src/
├── commonMain/kotlin/org/lanzadera/proyectos/
│   ├── di/AppModule.kt              # Koin DI configuration (single entry point)
│   ├── navigation/Navigation.kt     # NavHost with type-safe routes
│   ├── domain/
│   │   ├── models/                  # Domain entities (Movie, TvShow, Book, Game, FavoriteItem)
│   │   ├── repository/              # Abstract repository interfaces
│   │   └── usecase/                 # Business logic operations
│   ├── data/
│   │   ├── repository/              # Repository implementations
│   │   ├── datasource/              # Local/remote data sources + Provider functions
│   │   └── mapper/                  # Entity <-> Domain mappers
│   └── ui/
│       ├── screens/                 # Feature screens with ViewModels
│       └── components/              # Reusable Composables
├── androidMain/                     # Android-specific (Room database, providers)
└── commonTest/                      # Shared tests with Turbine + Truth
```

### Key Patterns

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

```properties
API_BEARER_TOKEN=your_tmdb_token
IGDB_CLIENT_ID=your_client_id
IGDB_CLIENT_SECRET=your_secret
```

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
