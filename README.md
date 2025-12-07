# 🎬 MovieApp - Kotlin Multiplatform

**A Clean Architecture multiplatform application for tracking movies, TV shows, books, and games.**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.6.0-green.svg)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean-orange.svg)](docs/architecture/ADR-001-Clean-Architecture.md)
[![Test Coverage](https://img.shields.io/badge/Coverage-~20%25-yellow.svg)](copilot/TESTING_COVERAGE.md)

---

## 📚 **IMPORTANT: Read Before Starting Any Session**

### ⚡ **Mandatory Session Initialization**

**EVERY Copilot session MUST start by reading all documentation:**

```bash
# 1. Read all project documentation
cat README.md
cat COPILOT.md
cat TODO_LIST.md
cat AUDIT_REPORT.md

# 2. Read architecture documentation
cat docs/architecture/ADR-001-Clean-Architecture.md
cat docs/architecture/ADR-002-Use-Case-Layer.md
cat docs/architecture/ADR-003-Repository-Pattern.md
cat docs/architecture/ADR-004-DTO-vs-Domain-Models.md

# 3. Read testing documentation
cat copilot/TESTING_STRATEGY.md
cat copilot/TESTING_COVERAGE.md
cat copilot/copilot-instructions.md

# 4. Check current progress
grep "Next Task:" TODO_LIST.md
grep "Overall Progress:" TODO_LIST.md
```

**Why?** This ensures you understand:
- ✅ Current architecture and design decisions
- ✅ Coding conventions and patterns
- ✅ What task to work on next
- ✅ Testing strategy and coverage goals

---

## 🚀 Quick Start

### Build & Run

```bash
# Android
./gradlew assembleDebug
./gradlew installDebug

# Desktop
./gradlew :composeApp:run

# iOS (requires macOS with Xcode)
cd iosApp && xcodebuild

# Tests
./gradlew :composeApp:allTests
```

### Project Health

| Metric | Current | Target | Status |
|--------|---------|--------|--------|
| **Overall Score** | 72/100 | 100/100 | 🟡 In Progress |
| **Architecture** | 75/100 | 100/100 | 🟡 Good |
| **Code Quality** | 65/100 | 100/100 | 🟡 Needs Work |
| **Test Coverage** | 20/100 | 80/100 | 🔴 Critical |
| **Best Practices** | 80/100 | 100/100 | 🟢 Good |

**Current Phase:** Phase 1 - Architecture Fixes (15/17 completed)  
**Next Task:** Task 1.15 - Document Architecture Decisions

---

## 🏗️ Architecture

This project follows **Clean Architecture** principles with strict layer separation:

```
┌─────────────────────────────────────────┐
│     UI Layer (Compose Multiplatform)   │
│   - Screens, ViewModels, UI Models     │
└──────────────────┬──────────────────────┘
                   │ depends on
┌──────────────────▼──────────────────────┐
│        Domain Layer (Pure Kotlin)       │
│   - Models, Repository Interfaces,      │
│     Use Cases                            │
└──────────────────┬──────────────────────┘
                   │ implements
┌──────────────────▼──────────────────────┐
│         Data Layer (Platform)           │
│   - Repository Implementations, DTOs,   │
│     Mappers, Data Sources                │
└─────────────────────────────────────────┘
```

**Key Principles:**
- 🎯 **Domain layer is pure Kotlin** - No framework dependencies
- 🔄 **DTOs in data layer** - Domain models have no `@Serializable`
- 📦 **Use cases for business logic** - ViewModels only inject use cases
- 🧪 **Repository pattern** - Interfaces in domain, implementations in data
- 🎨 **UI models** - UI layer has its own models, not domain models

**Read more:** [ADR-001: Clean Architecture](docs/architecture/ADR-001-Clean-Architecture.md)

---

## 📁 Project Structure

```
composeApp/src/
├── commonMain/kotlin/org/lanzadera/proyectos/
│   ├── domain/              # Business logic (pure Kotlin)
│   │   ├── models/          # Domain entities (NO @Serializable)
│   │   ├── repository/      # Repository interfaces
│   │   └── usecase/         # Business use cases
│   │
│   ├── data/                # Data layer
│   │   ├── dto/             # DTOs with @Serializable
│   │   ├── mapper/          # DTO ↔ Domain converters
│   │   ├── repository/      # Repository implementations
│   │   └── datasource/      # Platform-specific sources
│   │
│   ├── ui/                  # Presentation layer
│   │   ├── models/          # UI models (separate from domain)
│   │   ├── mapper/          # Domain → UI converters
│   │   ├── screens/         # Compose screens
│   │   ├── components/      # Reusable UI components
│   │   └── theme/           # Theming
│   │
│   ├── di/                  # Dependency injection (Koin)
│   └── utils/               # Shared utilities
│
├── androidMain/             # Android-specific code
├── iosMain/                 # iOS-specific code
├── desktopMain/             # Desktop-specific code
└── commonTest/              # Shared tests
    ├── base/                # Base test classes
    └── fakes/               # Fake implementations
```

---

## 🧪 Testing

### Test Coverage Goals

| Layer | Target | Current |
|-------|--------|---------|
| Use Cases | 80-90% | ~30% |
| Repositories | 70-80% | ~10% |
| ViewModels | 60-70% | ~15% |
| UI Components | 30-40% | ~5% |

### Running Tests

```bash
# All tests
./gradlew :composeApp:allTests

# Specific test
./gradlew test --tests "*ToggleFavoriteUseCaseTest"

# With coverage
./gradlew koverHtmlReport
open composeApp/build/reports/kover/html/index.html
```

**Read more:** [Testing Strategy](copilot/TESTING_STRATEGY.md)

---

## 📖 Documentation

### Architecture Decision Records (ADRs)
- [ADR-001: Clean Architecture](docs/architecture/ADR-001-Clean-Architecture.md)
- [ADR-002: Use Case Layer](docs/architecture/ADR-002-Use-Case-Layer.md)
- [ADR-003: Repository Pattern](docs/architecture/ADR-003-Repository-Pattern.md)
- [ADR-004: DTO vs Domain Models](docs/architecture/ADR-004-DTO-vs-Domain-Models.md)

### Development Guides
- [COPILOT.md](COPILOT.md) - Rules and conventions for development
- [TODO_LIST.md](TODO_LIST.md) - Task tracking (140 tasks total)
- [AUDIT_REPORT.md](AUDIT_REPORT.md) - Code audit findings

### Testing Guides
- [TESTING_STRATEGY.md](copilot/TESTING_STRATEGY.md) - Testing approach
- [TESTING_COVERAGE.md](copilot/TESTING_COVERAGE.md) - Coverage config

---

## 🔧 Technologies

### Core
- **Kotlin Multiplatform** - Shared business logic
- **Compose Multiplatform** - UI framework
- **Coroutines & Flow** - Async and reactive programming
- **Koin** - Dependency injection

### Networking
- **Ktor Client** - HTTP client
- **kotlinx.serialization** - JSON serialization

### Persistence
- **Room** (Android) - Local database
- **Realm** (iOS/Desktop) - Local database

### Testing
- **kotlin-test** - Test framework
- **Turbine** - Flow testing
- **Kover** - Code coverage

### APIs
- **TMDB** - Movies & TV shows
- **Google Books** - Books
- **IGDB** - Games

---

## 📊 Progress Tracking

**Overall:** 23/140 tasks completed (16.43%)

- ✅ **Phase 0:** Quick Wins (8/8) - **COMPLETE**
- 🟡 **Phase 1:** Architecture Fixes (15/17) - **IN PROGRESS**
- ⏳ **Phase 2:** Code Quality (0/12)
- ⏳ **Phase 3:** Testing - Use Cases & DTOs (0/19)
- ⏳ **Phase 4:** Testing - Repositories (0/15)
- ⏳ **Phase 5:** Testing - ViewModels (0/22)
- ⏳ **Phase 6:** UI Testing (0/18)
- ⏳ **Phase 7:** Integration & Polish (0/15)
- ⏳ **Phase 8:** Best Practices (0/18)

**See:** [TODO_LIST.md](TODO_LIST.md) for detailed task breakdown.

---

## 🤝 Contributing

### Before Making Changes

1. **Read all documentation** (see commands at top of README)
2. **Check TODO_LIST.md** for the next task
3. **Follow COPILOT.md** conventions
4. **Write tests** for all new code
5. **Update TODO_LIST.md** when completing tasks

### Code Style

- ✅ Follow Clean Architecture principles
- ✅ Use use cases for business logic
- ✅ Create UI models for presentation layer
- ✅ Write descriptive test names
- ✅ Use Napier for logging (no println)
- ✅ Document architectural decisions

---

## 📄 License

[License info here]

---

## 📞 Contact

[Contact info here]

---

**Last Updated:** December 7, 2025  
**Version:** 0.1.0 (In Development)
