package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.fakes.FakeFavoriteDetailsRepository
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import org.lanzadera.proyectos.fakes.FakeTvShowRepository
import org.lanzadera.proyectos.fakes.FakeWatchedEpisodesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ToggleTvShowFavoriteUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleTvShowFavoriteUseCase
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var fakeFavoriteDetailsRepository: FakeFavoriteDetailsRepository
    private lateinit var fakeTvShowRepository: FakeTvShowRepository
    private lateinit var fakeWatchedEpisodesRepository: FakeWatchedEpisodesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeFavoritesRepository = FakeFavoritesRepository()
        fakeFavoriteDetailsRepository = FakeFavoriteDetailsRepository()
        fakeTvShowRepository = FakeTvShowRepository()
        fakeWatchedEpisodesRepository = FakeWatchedEpisodesRepository()
        useCase = ToggleTvShowFavoriteUseCase(
            fakeFavoritesRepository,
            fakeFavoriteDetailsRepository,
            fakeTvShowRepository,
            fakeWatchedEpisodesRepository
        )
    }
    
    @Test
    fun `should add tv show to favorites and save details`() = runTest {
        // Given
        val tvShowItem = FavoriteItem(
            id = "456",
            title = "Test TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // Setup fake tv show details
        val tvShowDetails = org.lanzadera.proyectos.domain.models.tvshow.TvShow(
            id = 456,
            name = "Test TV Show",
            firstAirDate = "2024-01-01",
            numberOfEpisodes = 10
        )
        fakeTvShowRepository.setTvShowDetails(456, tvShowDetails)
        
        // When
        val result = useCase(tvShowItem)
        
        // Then
        assertTrue(result.isSuccess)
        
        // Verify TV show added to favorites
        assertTrue(fakeFavoritesRepository.isFavorite("456"))
        
        // Verify full details saved
        val savedTvShow = fakeFavoriteDetailsRepository.getFavoriteTvShow("456")
        assertNotNull(savedTvShow)
        assertEquals(456, savedTvShow.id)
        assertEquals("Test TV Show", savedTvShow.name)
    }
    
    @Test
    fun `should remove tv show from favorites and delete details and watched episodes`() = runTest {
        // Given - TV show already favorited with watched episodes
        val tvShowItem = FavoriteItem(
            id = "456",
            title = "Test TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        fakeFavoritesRepository.addFavorite(tvShowItem)
        
        // Add some watched episodes
        fakeWatchedEpisodesRepository.markEpisodeAsWatched("456", 1)
        fakeWatchedEpisodesRepository.markEpisodeAsWatched("456", 2)
        
        // Verify episodes exist before removal
        assertTrue(fakeWatchedEpisodesRepository.hasWatchedEpisode("456", 1))
        assertTrue(fakeWatchedEpisodesRepository.hasWatchedEpisode("456", 2))
        
        // When - Toggle to remove
        val result = useCase(tvShowItem)
        
        // Then
        assertTrue(result.isSuccess)
        
        // Verify TV show removed from favorites
        assertFalse(fakeFavoritesRepository.isFavorite("456"))
        
        // Verify details deleted
        val savedTvShow = fakeFavoriteDetailsRepository.getFavoriteTvShow("456")
        assertNull(savedTvShow)
        
        // Verify watched episodes deleted
        assertFalse(fakeWatchedEpisodesRepository.hasWatchedEpisode("456", 1))
        assertFalse(fakeWatchedEpisodesRepository.hasWatchedEpisode("456", 2))
    }
    
    @Test
    fun `should toggle tv show multiple times`() = runTest {
        // Given
        val tvShowItem = FavoriteItem(
            id = "789",
            title = "Test TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When - Add
        val result1 = useCase(tvShowItem)
        assertTrue(result1.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("789"))
        
        // When - Remove
        val result2 = useCase(tvShowItem)
        assertTrue(result2.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite("789"))
        
        // When - Add again
        val result3 = useCase(tvShowItem)
        assertTrue(result3.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("789"))
    }
    
    @Test
    fun `should handle invalid tv show ID gracefully`() = runTest {
        // Given - Invalid ID (not a number)
        val tvShowItem = FavoriteItem(
            id = "invalid",
            title = "Test TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When
        val result = useCase(tvShowItem)
        
        // Then - Should still succeed (toggle happens, just no details saved)
        assertTrue(result.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("invalid"))
    }
    
    @Test
    fun `should handle missing tv show details gracefully`() = runTest {
        // Given - TV show ID that doesn't exist in repository
        val tvShowItem = FavoriteItem(
            id = "999",
            title = "Non-existent TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When
        val result = useCase(tvShowItem)
        
        // Then - Should succeed with toggle but no details saved
        assertTrue(result.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("999"))
        
        // Details should not be saved
        val savedTvShow = fakeFavoriteDetailsRepository.getFavoriteTvShow("999")
        assertNull(savedTvShow)
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val tvShowItem = FavoriteItem(
            id = "456",
            title = "Test TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        fakeFavoritesRepository.shouldFail = true
        fakeFavoritesRepository.failureException = Exception("Database error")
        
        // When
        val result = useCase(tvShowItem)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `should throw IllegalArgumentException when type is not TV_SHOW`() = runTest {
        // Given - Wrong type
        val movieItem = FavoriteItem(
            id = "123",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When/Then
        try {
            useCase(movieItem)
            throw AssertionError("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Item must be of type TV_SHOW") == true)
        }
    }
}
