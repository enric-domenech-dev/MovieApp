package org.lanzadera.proyectos.ui.screens.detail

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.WatchedMovie
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleMovieFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.movies.GetMovieDetailsUseCase
import org.lanzadera.proyectos.domain.usecase.movies.ObserveWatchedMoviesUseCase
import org.lanzadera.proyectos.domain.usecase.movies.ToggleMovieWatchedUseCase
import org.lanzadera.proyectos.fakes.FakeFavoriteDetailsRepository
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import org.lanzadera.proyectos.fakes.FakeMovieRepository
import org.lanzadera.proyectos.fakes.FakeWatchedMoviesRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var fakeMovieRepository: FakeMovieRepository
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var fakeFavoriteDetailsRepository: FakeFavoriteDetailsRepository
    private lateinit var fakeWatchedMoviesRepository: FakeWatchedMoviesRepository
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var observeFavoritesUseCase: ObserveFavoritesUseCase
    private lateinit var toggleMovieFavoriteUseCase: ToggleMovieFavoriteUseCase
    private lateinit var observeWatchedMoviesUseCase: ObserveWatchedMoviesUseCase
    private lateinit var toggleMovieWatchedUseCase: ToggleMovieWatchedUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        fakeMovieRepository = FakeMovieRepository()
        fakeFavoritesRepository = FakeFavoritesRepository()
        fakeFavoriteDetailsRepository = FakeFavoriteDetailsRepository()
        fakeWatchedMoviesRepository = FakeWatchedMoviesRepository()
        
        getMovieDetailsUseCase = GetMovieDetailsUseCase(fakeMovieRepository)
        observeFavoritesUseCase = ObserveFavoritesUseCase(fakeFavoritesRepository)
        toggleMovieFavoriteUseCase = ToggleMovieFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository,
            favoriteDetailsRepository = fakeFavoriteDetailsRepository,
            movieRepository = fakeMovieRepository
        )
        observeWatchedMoviesUseCase = ObserveWatchedMoviesUseCase(fakeWatchedMoviesRepository)
        toggleMovieWatchedUseCase = ToggleMovieWatchedUseCase(fakeWatchedMoviesRepository)
        
        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            observeFavoritesUseCase = observeFavoritesUseCase,
            toggleMovieFavoriteUseCase = toggleMovieFavoriteUseCase,
            observeWatchedMoviesUseCase = observeWatchedMoviesUseCase,
            toggleMovieWatchedUseCase = toggleMovieWatchedUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is null`() = runTest {
        // Assert
        assertNull(viewModel.movieDetail.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadMovieDetails updates state on success`() = runTest {
        // Arrange
        val testMovie = Movie(
            id = 1,
            title = "Test Movie",
            overview = "Test overview",
            releaseDate = "2020-01-01"
        )
        fakeMovieRepository.setMovieDetails(1, testMovie)
        
        // Act
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertNotNull(viewModel.movieDetail.value)
        assertEquals(1, viewModel.movieDetail.value?.id)
        assertEquals("Test Movie", viewModel.movieDetail.value?.title)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadMovieDetails sets error when result is null`() = runTest {
        // Arrange
        fakeMovieRepository.setMovieDetails(1, null)
        
        // Act
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertNull(viewModel.movieDetail.value)
        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains("No se pudieron cargar"))
    }

    @Test
    fun `loadMovieDetails sets error on repository failure`() = runTest {
        // Arrange
        fakeMovieRepository.shouldFail = true
        
        // Act
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertNull(viewModel.movieDetail.value)
        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `setMovieDetail updates state and loads full details`() = runTest {
        // Arrange
        val testMovie = Movie(
            id = 1,
            title = "Test Movie",
            overview = "Test overview"
        )
        val fullMovie = Movie(
            id = 1,
            title = "Test Movie",
            overview = "Full overview with cast"
        )
        fakeMovieRepository.setMovieDetails(1, fullMovie)
        
        // Act
        viewModel.setMovieDetail(testMovie)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert - should have full details loaded
        assertNotNull(viewModel.movieDetail.value)
        assertEquals(1, viewModel.movieDetail.value?.id)
    }

    @Test
    fun `toggleFavorite adds movie to favorites`() = runTest {
        // Arrange
        val testMovie = Movie(
            id = 1,
            title = "Test Movie",
            overview = "Test overview",
            posterPath = "/test.jpg"
        )
        fakeMovieRepository.setMovieDetails(1, testMovie)
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Act
        viewModel.toggleFavorite()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert - check that favorites list is not empty
        val favorites = viewModel.favorites.value
        assertFalse(favorites.isEmpty())
    }

    @Test
    fun `toggleFavorite does nothing when movie is null`() = runTest {
        // Arrange - no movie set
        
        // Act
        viewModel.toggleFavorite()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val favorites = viewModel.favorites.value
        assertTrue(favorites.isEmpty())
    }

    @Test
    fun `toggleWatched marks movie as watched`() = runTest {
        // Arrange
        val testMovie = Movie(
            id = 1,
            title = "Test Movie",
            releaseDate = "2020-01-01" // Past date
        )
        fakeMovieRepository.setMovieDetails(1, testMovie)
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Act
        viewModel.toggleWatched()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val watched = viewModel.isWatched.value
        assertTrue(watched)
    }

    @Test
    fun `toggleWatched unmarks watched movie`() = runTest {
        // Arrange
        val testMovie = Movie(
            id = 1,
            title = "Test Movie",
            releaseDate = "2020-01-01"
        )
        fakeMovieRepository.setMovieDetails(1, testMovie)
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Mark as watched first
        fakeWatchedMoviesRepository.addWatchedMovie(WatchedMovie(movieId = "1"))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Act - toggle off
        viewModel.toggleWatched()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val watched = viewModel.isWatched.value
        assertFalse(watched)
    }

    @Test
    fun `toggleWatched does nothing for unreleased movie when not watched`() = runTest {
        // Arrange
        val futureMovie = Movie(
            id = 1,
            title = "Future Movie",
            releaseDate = "2099-12-31" // Future date
        )
        fakeMovieRepository.setMovieDetails(1, futureMovie)
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Act - try to mark unreleased movie as watched
        viewModel.toggleWatched()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert - should remain unwatched
        val watched = viewModel.isWatched.value
        assertFalse(watched)
    }

    @Test
    fun `isReleased returns true for past release date`() = runTest {
        // Arrange
        val testMovie = Movie(
            id = 1,
            title = "Released Movie",
            releaseDate = "2020-01-01"
        )
        fakeMovieRepository.setMovieDetails(1, testMovie)
        
        // Act
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val released = viewModel.isReleased.value
        assertTrue(released)
    }

    @Test
    fun `isReleased returns false for future release date`() = runTest {
        // Arrange
        val futureMovie = Movie(
            id = 1,
            title = "Upcoming Movie",
            releaseDate = "2099-12-31"
        )
        fakeMovieRepository.setMovieDetails(1, futureMovie)
        
        // Act
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        val released = viewModel.isReleased.value
        assertFalse(released)
    }

    @Test
    fun `loading multiple movies updates state correctly`() = runTest {
        // Arrange
        val movie1 = Movie(id = 1, title = "Movie 1")
        val movie2 = Movie(id = 2, title = "Movie 2")
        fakeMovieRepository.setMovieDetails(1, movie1)
        fakeMovieRepository.setMovieDetails(2, movie2)
        
        // Act
        viewModel.loadMovieDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals("Movie 1", viewModel.movieDetail.value?.title)
        
        viewModel.loadMovieDetails(2)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals("Movie 2", viewModel.movieDetail.value?.title)
    }
}
