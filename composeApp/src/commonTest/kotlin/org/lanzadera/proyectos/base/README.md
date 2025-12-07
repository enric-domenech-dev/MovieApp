# Base Test Classes

This package provides base classes and utilities for writing tests in the MovieApp project.

## Overview

- **ViewModelTest** - Base class for testing ViewModels
- **RepositoryTest** - Base class for testing Repositories
- **UseCaseTest** - Base class for testing Use Cases
- **TestData** - Factory for creating test domain objects

## Usage

### ViewModelTest

Extend this class when testing ViewModels. It automatically sets up the test dispatcher for coroutines.

```kotlin
class MyViewModelTest : ViewModelTest() {
    private lateinit var viewModel: MyViewModel
    private lateinit var fakeUseCase: FakeUseCase
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeUseCase = FakeUseCase()
        viewModel = MyViewModel(fakeUseCase)
    }
    
    @Test
    fun `should load data on init`() = runTest {
        viewModel.uiState.test {
            // Verify initial state
            assertTrue(awaitItem().isLoading)
            
            // Verify loaded state
            val loadedState = awaitItem()
            assertTrue(loadedState.isSuccess)
            assertEquals(expectedData, loadedState.data)
        }
    }
}
```

### RepositoryTest

Extend this class when testing Repositories. Useful for testing data layer logic with suspend functions and flows.

```kotlin
class MovieRepositoryTest : RepositoryTest() {
    private lateinit var repository: MovieRepository
    private lateinit var mockHttpClient: HttpClient
    
    @BeforeTest
    override fun setup() {
        super.setup()
        mockHttpClient = createMockHttpClient()
        repository = MovieRepositoryImpl(mockHttpClient, maxPages = 5, json)
    }
    
    @Test
    fun `should fetch movies successfully`() = runTest {
        repository.moviesFlow.test {
            repository.refreshMovies(force = true)
            
            val movies = awaitItem()
            assertEquals(expectedMovies, movies)
        }
    }
}
```

### UseCaseTest

Extend this class when testing Use Cases. Perfect for testing business logic with suspend functions.

```kotlin
class ToggleFavoriteUseCaseTest : UseCaseTest() {
    private lateinit var useCase: ToggleFavoriteUseCase
    private lateinit var fakeRepository: FakeFavoritesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeRepository = FakeFavoritesRepository()
        useCase = ToggleFavoriteUseCase(fakeRepository, ...)
    }
    
    @Test
    fun `should toggle favorite successfully`() = runTest {
        val item = TestData.testFavoriteMovie
        
        val result = useCase(item)
        
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.isFavorite(item.id))
    }
}
```

### TestData

Use the TestData object to get pre-configured test domain objects:

```kotlin
@Test
fun `test with sample data`() {
    val movie = TestData.testMovie
    val tvShow = TestData.testTvShow
    val book = TestData.testBook
    val game = TestData.testGame
    
    // Or create custom instances
    val customMovie = TestData.createMovie(
        id = 999,
        title = "Custom Movie",
        voteAverage = 9.5
    )
}
```

## Best Practices

1. **Always call super.setup()** - If you override `setup()`, make sure to call `super.setup()` first
2. **Always call super.tearDown()** - If you override `tearDown()`, make sure to call `super.tearDown()` last
3. **Use runTest** - Wrap your test body with `runTest { }` for coroutine tests
4. **Use Turbine** - Use `.test { }` extension for Flow testing
5. **Use TestData** - Prefer TestData objects over creating domain objects manually
6. **Use Fakes, not Mocks** - Create fake implementations of repositories instead of mocking

## Example Test Structure

```kotlin
class ExampleTest : UseCaseTest() {
    // 1. Declare dependencies
    private lateinit var useCase: ExampleUseCase
    private lateinit var fakeRepository: FakeRepository
    
    // 2. Setup
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeRepository = FakeRepository()
        useCase = ExampleUseCase(fakeRepository)
    }
    
    // 3. Tests
    @Test
    fun `should do something successfully`() = runTest {
        // Given
        val input = TestData.testMovie
        
        // When
        val result = useCase(input)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }
    
    @Test
    fun `should handle error gracefully`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        
        // When
        val result = useCase(TestData.testMovie)
        
        // Then
        assertTrue(result.isError)
        assertNotNull(result.exceptionOrNull())
    }
}
```

## Dependencies

These base classes use:
- `kotlin-test` - For test annotations and assertions
- `kotlinx-coroutines-test` - For testing coroutines
- `turbine` - For testing Flows (recommended)
- `truth` - For better assertions (optional)
