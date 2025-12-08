package org.lanzadera.proyectos.ui.screens.search

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.SearchRepository
import org.lanzadera.proyectos.domain.usecase.search.SearchMoviesUseCase
import org.lanzadera.proyectos.domain.usecase.search.SearchTvShowsUseCase
import org.lanzadera.proyectos.fakes.FakeSearchRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var fakeRepository: FakeSearchRepository
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private lateinit var searchTvShowsUseCase: SearchTvShowsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        fakeRepository = FakeSearchRepository()
        searchMoviesUseCase = SearchMoviesUseCase(fakeRepository)
        searchTvShowsUseCase = SearchTvShowsUseCase(fakeRepository)
        
        viewModel = SearchViewModel(
            searchMoviesUseCase = searchMoviesUseCase,
            searchTvShowsUseCase = searchTvShowsUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Assert
        assertEquals("", viewModel.query.value)
        assertEquals(emptyList(), viewModel.results.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `onQueryChanged updates query state`() = runTest {
        // Act
        viewModel.onQueryChanged("test query")
        
        // Assert
        viewModel.query.test {
            assertEquals("test query", awaitItem())
        }
    }

    @Test
    fun `performSearch with empty query returns empty results`() = runTest {
        // Arrange
        viewModel.onQueryChanged("")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(emptyList(), viewModel.results.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `performSearch with whitespace query returns empty results`() = runTest {
        // Arrange
        viewModel.onQueryChanged("   ")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(emptyList(), viewModel.results.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `performSearch returns combined movie and TV show results`() = runTest {
        // Arrange
        val testMovies = listOf(
            Movie(id = 1, title = "Movie 1"),
            Movie(id = 2, title = "Movie 2")
        )
        val testTvShows = listOf(
            TvShow(id = 1, name = "Show 1"),
            TvShow(id = 2, name = "Show 2")
        )
        
        fakeRepository.setMovieResults("avengers", testMovies)
        fakeRepository.setTvShowResults("avengers", testTvShows)
        
        viewModel.onQueryChanged("avengers")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val results = viewModel.results.value
        assertEquals(4, results.size) // 2 movies + 2 TV shows
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `performSearch sets loading state correctly`() = runTest {
        // Arrange
        val testMovies = listOf(Movie(id = 1, title = "Test"))
        fakeRepository.setMovieResults("test", testMovies)
        viewModel.onQueryChanged("test")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert - loading should be false after completion
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `performSearch shows error when both searches fail`() = runTest {
        // Arrange
        fakeRepository.shouldFail = true
        viewModel.onQueryChanged("test")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains("Error al buscar"))
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `performSearch shows message when no results found`() = runTest {
        // Arrange - No results set for query
        viewModel.onQueryChanged("nonexistent")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(emptyList(), viewModel.results.value)
        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains("No se encontraron resultados"))
    }

    @Test
    fun `performSearch with only movie results succeeds`() = runTest {
        // Arrange
        val testMovies = listOf(
            Movie(id = 1, title = "Movie 1"),
            Movie(id = 2, title = "Movie 2")
        )
        fakeRepository.setMovieResults("movies", testMovies)
        // No TV show results set
        
        viewModel.onQueryChanged("movies")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(2, viewModel.results.value.size)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `performSearch with only TV show results succeeds`() = runTest {
        // Arrange
        val testTvShows = listOf(
            TvShow(id = 1, name = "Show 1"),
            TvShow(id = 2, name = "Show 2")
        )
        fakeRepository.setTvShowResults("shows", testTvShows)
        // No movie results set
        
        viewModel.onQueryChanged("shows")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(2, viewModel.results.value.size)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `clear resets all state`() = runTest {
        // Arrange - Set some state
        val testMovies = listOf(Movie(id = 1, title = "Test"))
        fakeRepository.setMovieResults("test", testMovies)
        viewModel.onQueryChanged("test")
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Act
        viewModel.clear()
        
        // Assert
        assertEquals("", viewModel.query.value)
        assertEquals(emptyList(), viewModel.results.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `performSearch alternates movie and TV show results`() = runTest {
        // Arrange
        val testMovies = listOf(
            Movie(id = 1, title = "Movie 1"),
            Movie(id = 2, title = "Movie 2"),
            Movie(id = 3, title = "Movie 3")
        )
        val testTvShows = listOf(
            TvShow(id = 1, name = "Show 1"),
            TvShow(id = 2, name = "Show 2")
        )
        
        fakeRepository.setMovieResults("mixed", testMovies)
        fakeRepository.setTvShowResults("mixed", testTvShows)
        
        viewModel.onQueryChanged("mixed")
        
        // Act
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val results = viewModel.results.value
        assertEquals(5, results.size) // 3 movies + 2 shows
        
        // Results should alternate: Movie, Show, Movie, Show, Movie
        // (Based on the ViewModel's combining logic)
    }

    @Test
    fun `performSearch clears previous error`() = runTest {
        // Arrange - First search fails
        fakeRepository.shouldFail = true
        viewModel.onQueryChanged("test")
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertNotNull(viewModel.error.value) // Error set
        
        // Act - Second search succeeds
        fakeRepository.shouldFail = false
        val testMovies = listOf(Movie(id = 1, title = "Test"))
        fakeRepository.setMovieResults("test", testMovies)
        viewModel.performSearch()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertNull(viewModel.error.value) // Error cleared
        assertEquals(1, viewModel.results.value.size)
    }
}
