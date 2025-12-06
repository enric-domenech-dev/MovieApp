# GitHub Copilot Instructions

## Project Context

This is a **Kotlin Multiplatform Compose** application.

**Current Project Health:**

- Overall Score: 72/100
- Architecture & Design: 75/100
- Code Quality: 65/100
- Testing & Coverage: 45/100 (~5% coverage)
- Best Practices: 80/100

## 🔄 Working with TODO List (Multi-Session Approach)

**We're iterating through issues systematically across multiple Copilot sessions.**

### At Session Start:

1. Read TODO_LIST.md to see current progress
2. Find the next uncompleted `[ ]` task in the active phase
3. Review the task's context in AUDIT_REPORT.md if needed
4. Check COPILOT.md for architecture patterns

### During Session:

- Focus on completing 1-3 tasks depending on session length
- Follow subtasks step-by-step, checking off each `[ ]` as completed
- Test after each subtask when possible
- Don't rush - quality over quantity

### At Session End:

- Mark completed tasks with `[x]` in TODO_LIST.md
- Update progress counters in TODO_LIST.md
- Add session entry to "SESSION LOG" section
- Commit with format: `"Task X.Y: [description]"`
- Document any blockers or notes for next session

### Multi-Session Strategy:

- **Session 1-3:** Phase 0 (Quick Wins) - Foundation work
- **Session 4-8:** Phase 1 (Architecture) - Big refactoring
- **Session 9-12:** Phase 2 (Code Quality) - Error handling
- **Session 13-20:** Phase 3-4 (Testing) - Use cases & repositories
- **Session 21-30:** Phase 5 (ViewModels) - UI logic testing
- **Session 31-35:** Phase 6-7 (UI & Integration) - End-to-end
- **Session 36-40:** Phase 8 (Best Practices) - Polish

**💡 Remember:** Each task is designed to be completable in one session. If a task is too large, break it down further in
the subtasks.

## Development Guidelines for Copilot

### Code Style & Conventions

- **Language**: Kotlin with idiomatic patterns (data classes, sealed classes, extension functions)
- **Compose**: Use declarative UI patterns, avoid side effects in composables
- **Naming**:
    - ViewModels end with `ViewModel` (e.g., `MoviesViewModel`)
    - Use cases end with `UseCase` (e.g., `ToggleFavoriteUseCase`)
    - Repositories end with `Repository` (e.g., `FavoritesRepository`)
- **Null Safety**: Prefer non-null types, use `?` and `?.let {}` when necessary
- **Immutability**: Use `val` over `var`, immutable collections by default

### Architecture Patterns

1. **Clean Architecture Layers**:
    - `domain/` - Pure Kotlin, no platform dependencies
    - `data/` - Implementations with platform-specific code via expect/actual
    - `ui/` - Compose UI with ViewModels

2. **Dependency Injection**:
    - All dependencies registered in `di/AppModule.kt`
    - Use constructor injection
    - Example:
   ```kotlin
   class MyViewModel(
       private val useCase: MyUseCase
   ) : ViewModel()
   ```

3. **State Management**:
    - ViewModels expose `StateFlow<UiState>` for UI state
    - Repositories return `Flow<T>` for reactive data
    - Use `.stateIn()` to convert Flow to StateFlow in ViewModels

4. **Navigation**:
    - Type-safe navigation with serializable routes
    - Define routes in `navigation/Navigation.kt`

### Testing

- Write tests in `commonTest/` for multiplatform code
- Use **Turbine** for Flow testing:
  ```kotlin
  flow.test {
      assertEquals(expected, awaitItem())
      cancelAndIgnoreRemainingEvents()
  }
  ```
- Use **Truth** assertions: `assertThat(value).isEqualTo(expected)`
- Create fakes instead of mocks when possible

### When Adding New Features

1. **Start with Domain**: Define models and repository interface
2. **Implement Data Layer**: Create repository implementation and data sources
3. **Add to DI**: Register in `AppModule.kt`
4. **Build UI**: Create screen composable and ViewModel
5. **Write Tests**: Add tests for use cases and repositories

### Database Operations

- **Room** (Android only): Define entities with `@Entity`, DAOs with `@Dao`
- **Schema Location**: `composeApp/schemas/` for Room schema exports
- **Migrations**: Export schema and handle migrations explicitly
- **Fallbacks**: Provide in-memory implementations for non-Android platforms

### API Integration

- Use **Ktor** clients defined in `AppModule.kt`
- API keys from `local.properties`, never hardcode credentials
- Create DTOs for API responses in `data/` layer
- Map DTOs to domain models using mapper functions

### Common Mistakes to Avoid

❌ Don't put platform-specific code in `commonMain` without expect/actual
❌ Don't collect Flows in composables without lifecycle awareness
❌ Don't create ViewModels without registering in Koin
❌ Don't expose mutable state from ViewModels (use `StateFlow`, not `MutableStateFlow`)
❌ Don't perform business logic in ViewModels (use use cases)

### Code Generation Preferences

When generating code:

- Prefer concise Kotlin syntax over verbose Java-style
- Use `when` expressions instead of if-else chains for enums/sealed classes
- Use trailing lambdas and named arguments for readability
- Add KDoc only for public APIs, avoid obvious comments
- Use `TODO()` with descriptive messages for unimplemented features

### Build & Run Shortcuts

```bash
# Quick rebuild after code changes
./gradlew :composeApp:assembleDebug --no-build-cache

# Run tests for specific feature
./gradlew :composeApp:testDebugUnitTest --tests "*MoviesViewModelTest"

# Clean build (if facing weird issues)
./gradlew clean build
```

## Asking Copilot for Help

**Good prompts:**

- "Add a new use case to fetch trending movies"
- "Create a ViewModel test for the favorites screen"
- "Implement repository for game details with IGDB API"
- "Add Room entity for user preferences"
- "Help me with task X.Y from TODO_LIST.md"
- "Fix issue #N from AUDIT_REPORT.md"

**Be specific about:**

- Which layer (domain/data/ui) the code should go in
- Whether it's Android-specific or multiplatform
- Expected data flow (Flow, StateFlow, suspend function)
- Reference specific tasks from TODO_LIST.md when applicable

## Quick References

**To check project health:**

```bash
# Run audit command
gh copilot run audit

# Check test coverage
./gradlew koverHtmlReport

# View TODO progress
grep -E "Overall Progress:" TODO_LIST.md
grep -E "\[.\] \*\*Phase" TODO_LIST.md

# Count pending tasks
grep "\[ \]" TODO_LIST.md | wc -l

# Count completed tasks  
grep "\[x\]" TODO_LIST.md | wc -l
```

**To find next task to work on:**

```bash
# Show next uncompleted task
grep -A 3 "^## Task.*\[ \]" TODO_LIST.md | head -15

# Or just ask Copilot:
# "What's the next task I should work on?"
```

**Session Workflow Commands:**

```bash
# At session start
cat TODO_LIST.md | grep "Next Task:"

# After completing a task (update progress)
# Manually update TODO_LIST.md:
# - Change [ ] to [x] for completed task
# - Update phase counter (e.g., 0/8 → 1/8)
# - Update overall progress (0/135 → 1/135)
# - Update "Next Task" field
# - Add session log entry

# Commit your work
git add TODO_LIST.md [other files]
git commit -m "Task X.Y: [description]"
```

**Progress Tracking:**

- Each completed task = +0.74% towards 100% (135 total tasks)
- Each completed phase shows tangible score improvement
- Update "SESSION LOG" in TODO_LIST.md after each session
