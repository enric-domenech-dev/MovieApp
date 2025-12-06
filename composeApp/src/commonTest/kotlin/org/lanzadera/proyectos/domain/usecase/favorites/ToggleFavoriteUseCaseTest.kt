package org.lanzadera.proyectos.domain.usecase.favorites

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.TestData
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.fakes.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToggleFavoriteUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleFavoriteUseCase
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var fakeFavoriteDetailsRepository: FakeFavoriteDetailsRepository
    private lateinit var fakeWatchedEpisodesRepository: FakeWatchedEpisodesRepository
    private lateinit var fakeTvShowRepository: FakeTvShowRepository
    private lateinit var fakeMovieRepository: FakeMovieRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeFavoritesRepository = FakeFavoritesRepository()
        fakeFavoriteDetailsRepository = FakeFavoriteDetailsRepository()
        fakeWatchedEpisodesRepository = FakeWatchedEpisodesRepository()
        fakeTvShowRepository = FakeTvShowRepository()
        fakeMovieRepository = FakeMovieRepository()
        
        useCase = ToggleFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository,
            favoriteDetailsRepository = fakeFavoriteDetailsRepository,
            watchedEpisodesRepository = fakeWatchedEpisodesRepository,
            tvShowRepository = fakeTvShowRepository,
            movieRepository = fakeMovieRepository
        )
    }
    
    @Test
    fun `should add movie to favorites when not present`() = runTest {
        // Given
        val movie = TestData.testFavoriteMovie
        
        // When
        val result = useCase(movie)
        
        // Then
        assertTrue(result.isSuccess)
        fakeFavoritesRepository.favorites.test {
            val favorites = awaitItem()
            assertTrue(favorites.any { it.id == movie.id && it.type == movie.type })
        }
    }
    
    @Test
    fun `should remove movie from favorites when already present`() = runTest {
        // Given
        val movie = TestData.testFavoriteMovie
        fakeFavoritesRepository.addFavorite(movie)
        
        // When
        val result = useCase(movie)
        
        // Then
        assertTrue(result.isSuccess)
        fakeFavoritesRepository.favorites.test {
            val favorites = awaitItem()
            assertFalse(favorites.any { it.id == movie.id && it.type == movie.type })
        }
    }
    
    @Test
    fun `should add tv show to favorites and remove episodes when toggled off`() = runTest {
        // Given
        val tvShow = TestData.testFavoriteTvShow
        fakeWatchedEpisodesRepository.markEpisodeAsWatched(tvShow.id, 1)
        fakeWatchedEpisodesRepository.markEpisodeAsWatched(tvShow.id, 2)
        fakeFavoritesRepository.addFavorite(tvShow)
        
        // When - Toggle off (remove from favorites)
        val result = useCase(tvShow)
        
        // Then
        assertTrue(result.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite(tvShow.id))
        assertFalse(fakeWatchedEpisodesRepository.hasWatchedEpisode(tvShow.id, 1))
        assertFalse(fakeWatchedEpisodesRepository.hasWatchedEpisode(tvShow.id, 2))
    }
    
    @Test
    fun `should handle book and game favorites`() = runTest {
        // Given
        val book = TestData.testFavoriteBook
        val game = TestData.testFavoriteGame
        
        // When
        val bookResult = useCase(book)
        val gameResult = useCase(game)
        
        // Then
        assertTrue(bookResult.isSuccess)
        assertTrue(gameResult.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite(book.id))
        assertTrue(fakeFavoritesRepository.isFavorite(game.id))
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val movie = TestData.testFavoriteMovie
        fakeFavoritesRepository.shouldFail = true
        fakeFavoritesRepository.failureException = Exception("Network error")
        
        // When
        val result = useCase(movie)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `should handle multiple toggle operations`() = runTest {
        // Given
        val movie = TestData.testFavoriteMovie
        
        // When - Toggle on
        val result1 = useCase(movie)
        assertTrue(result1.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite(movie.id))
        
        // When - Toggle off
        val result2 = useCase(movie)
        assertTrue(result2.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite(movie.id))
        
        // When - Toggle on again
        val result3 = useCase(movie)
        assertTrue(result3.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite(movie.id))
    }
}
