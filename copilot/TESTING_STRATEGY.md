# Testing Strategy

## Overview

This document defines the testing strategy for the MovieApp project, a Kotlin Multiplatform application.

## Testing Pyramid

```
        /\
       /  \      E2E Tests (Few)
      /----\     
     /      \    Integration Tests (Some)
    /--------\   
   /          \  Unit Tests (Many)
  /____________\ 
```

### Distribution

- **Unit Tests**: 70% - Fast, isolated, test business logic
- **Integration Tests**: 20% - Test component integration
- **E2E Tests**: 10% - Test complete user flows

## Test Layers

### 1. Domain Layer (HIGH Priority)

**What to Test:**

- ✅ Use Cases - Business logic, error handling, data transformation
- ✅ Domain Models - Complex validation logic

**Coverage Target**: 80-90%

**Example:**

```kotlin
class ToggleFavoriteUseCaseTest : UseCaseTest() {
    @Test
    fun `should add movie to favorites`() = runTest {
        val result = useCase(movie)
        assertTrue(result.isSuccess)
    }
}
```

### 2. Data Layer (HIGH Priority)

**What to Test:**

- ✅ Repositories - Data fetching, caching, error handling
- ✅ Data Sources - API integration, database operations
- ✅ Mappers - DTO to Domain model conversion

**Coverage Target**: 70-80%

**Example:**

```kotlin
class MovieRepositoryTest : RepositoryTest() {
    @Test
    fun `should fetch movies from API`() = runTest {
        repository.moviesFlow.test {
            assertEquals(expectedMovies, awaitItem())
        }
    }
}
```

### 3. Presentation Layer (MEDIUM Priority)

**What to Test:**

- ✅ ViewModels - State management, user actions
- ❌ Composables (unless complex logic)

**Coverage Target**: 60-70%

**Example:**

```kotlin
class HomeViewModelTest : ViewModelTest() {
    @Test
    fun `should load movies on init`() = runTest {
        viewModel.uiState.test {
            assertTrue(awaitItem().isLoading)
            assertTrue(awaitItem().isSuccess)
        }
    }
}
```

### 4. UI Layer (LOW Priority)

**What to Test:**

- ✅ Complex UI logic
- ❌ Simple composables
- ❌ Theme/styling

**Coverage Target**: 30-40%

## Testing Tools

### Frameworks

- **kotlin-test**: Test annotations and assertions
- **kotlinx-coroutines-test**: TestDispatcher, runTest
- **Turbine**: Flow testing
- **Truth**: Better assertions (optional)

### Test Doubles

- **Fakes**: Preferred over mocks (in-memory implementations)
- **Mocks**: Only when fakes are impractical

### Coverage

- **Kover**: Code coverage reporting
- **Minimum**: 20% → **Target**: 70-80%

## Test Organization

```
commonTest/
├── base/
│   ├── ViewModelTest.kt      # Base class for ViewModels
│   ├── RepositoryTest.kt     # Base class for Repositories
│   ├── UseCaseTest.kt        # Base class for Use Cases
│   ├── TestData.kt           # Sample test data
│   └── README.md             # How to use base classes
│
├── fakes/
│   ├── FakeFavoritesRepository.kt
│   ├── FakeMovieRepository.kt
│   └── ...                   # One fake per repository
│
└── domain/
    ├── usecase/
    │   ├── favorites/
    │   │   └── ToggleFavoriteUseCaseTest.kt
    │   └── movies/
    │       └── ToggleMovieWatchedUseCaseTest.kt
    └── repository/
        └── MovieRepositoryTest.kt
```

## Testing Principles

### 1. AAA Pattern (Arrange-Act-Assert)

```kotlin
@Test
fun `should do something`() = runTest {
    // Arrange (Given)
    val input = TestData.testMovie
    
    // Act (When)
    val result = useCase(input)
    
    // Assert (Then)
    assertTrue(result.isSuccess)
}
```

### 2. Test Names

Use descriptive names that explain:

- **What** is being tested
- **When** (under what conditions)
- **Expected outcome**

```kotlin
// ✅ Good
@Test
fun `should return error when repository fails`()

// ❌ Bad
@Test
fun test1()
```

### 3. One Assertion Focus

Each test should verify ONE specific behavior.

```kotlin
// ✅ Good - One behavior
@Test
fun `should add movie to favorites`()

@Test
fun `should remove movie from favorites`()

// ❌ Bad - Multiple behaviors
@Test
fun `should toggle favorites`()
```

### 4. Use Test Data Factory

```kotlin
// ✅ Good
val movie = TestData.testMovie

// ❌ Bad - Creating manually
val movie = Movie(id = 1, title = "Test", ...)
```

### 5. Test Error Paths

```kotlin
@Test
fun `should return error when network fails`() = runTest {
    fakeRepository.shouldFail = true
    
    val result = useCase()
    
    assertTrue(result.isError)
    assertNotNull(result.exceptionOrNull())
}
```

## Testing Workflow

### 1. Development

```bash
# Run tests continuously
./gradlew test --continuous

# Run specific test
./gradlew test --tests "*ToggleFavoriteUseCaseTest"
```

### 2. Pre-Commit

```bash
# Run all tests
./gradlew test

# Check coverage
./gradlew koverVerify
```

### 3. CI/CD

```bash
# Run tests with coverage
./gradlew test koverXmlReport

# Upload to coverage service
# (Codecov, Coveralls, etc.)
```

## Test Phases

### Phase 0 (Current) ✅

- [x] Setup base test classes
- [x] Setup Kover coverage
- [x] Test 3 critical use cases (14 tests)
- [x] Document testing strategy
- **Status**: COMPLETE

### Phase 3 (Testing Focus)

- [ ] Test all 30+ use cases
- [ ] Test all repositories
- [ ] Add integration tests
- **Target**: 40-50% coverage

### Phase 5 (ViewModel Testing)

- [ ] Test all ViewModels
- [ ] Add ViewModel integration tests
- **Target**: 60% coverage

### Phase 6 (UI Testing)

- [ ] Add Compose UI tests
- [ ] Add screenshot tests
- **Target**: 70% coverage

## Best Practices Summary

✅ **DO:**

- Use base test classes (ViewModelTest, UseCaseTest, etc.)
- Use fakes instead of mocks
- Test error paths
- Use descriptive test names
- Keep tests fast (<100ms per test)
- Use TestData factory
- Follow AAA pattern

❌ **DON'T:**

- Test Android framework code
- Test third-party libraries
- Test simple getters/setters
- Share state between tests
- Use production database/network in tests
- Write tests that depend on each other

## Resources

- Base Test Classes: `composeApp/src/commonTest/kotlin/org/lanzadera/proyectos/base/README.md`
- Coverage Config: `docs/TESTING_COVERAGE.md`
- Kover Docs: https://github.com/Kotlin/kotlinx-kover
