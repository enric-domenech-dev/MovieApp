package org.lanzadera.proyectos.domain.usecase.episodes

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.fakes.FakeWatchedEpisodesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToggleEpisodeWatchedUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleEpisodeWatchedUseCase
    private lateinit var fakeRepository: FakeWatchedEpisodesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeRepository = FakeWatchedEpisodesRepository()
        useCase = ToggleEpisodeWatchedUseCase(fakeRepository)
    }
    
    @Test
    fun `should mark episode as watched`() = runTest {
        // Given
        val episode = WatchedEpisode(
            tvShowId = "100",
            seasonNumber = 1,
            episodeNumber = 1
        )
        
        // When
        val result = useCase(episode, isWatched = true)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.hasWatchedEpisode("100", 1))
    }
    
    @Test
    fun `should mark episode as unwatched`() = runTest {
        // Given
        val episode = WatchedEpisode(
            tvShowId = "100",
            seasonNumber = 1,
            episodeNumber = 1
        )
        fakeRepository.markEpisodeAsWatched("100", 1)
        
        // When
        val result = useCase(episode, isWatched = false)
        
        // Then
        assertTrue(result.isSuccess)
        assertFalse(fakeRepository.hasWatchedEpisode("100", 1))
    }
    
    @Test
    fun `should toggle episode watched status multiple times`() = runTest {
        // Given
        val episode = WatchedEpisode(
            tvShowId = "200",
            seasonNumber = 2,
            episodeNumber = 5
        )
        
        // When/Then - Mark as watched
        val result1 = useCase(episode, isWatched = true)
        assertTrue(result1.isSuccess)
        assertTrue(fakeRepository.hasWatchedEpisode("200", 5))
        
        // When/Then - Mark as unwatched
        val result2 = useCase(episode, isWatched = false)
        assertTrue(result2.isSuccess)
        assertFalse(fakeRepository.hasWatchedEpisode("200", 5))
        
        // When/Then - Mark as watched again
        val result3 = useCase(episode, isWatched = true)
        assertTrue(result3.isSuccess)
        assertTrue(fakeRepository.hasWatchedEpisode("200", 5))
    }
    
    @Test
    fun `should handle multiple episodes for same TV show`() = runTest {
        // Given
        val episode1 = WatchedEpisode(
            tvShowId = "100",
            seasonNumber = 1,
            episodeNumber = 1
        )
        val episode2 = WatchedEpisode(
            tvShowId = "100",
            seasonNumber = 1,
            episodeNumber = 2
        )
        
        // When
        val result1 = useCase(episode1, isWatched = true)
        val result2 = useCase(episode2, isWatched = true)
        
        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(fakeRepository.hasWatchedEpisode("100", 1))
        assertTrue(fakeRepository.hasWatchedEpisode("100", 2))
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val episode = WatchedEpisode(
            tvShowId = "100",
            seasonNumber = 1,
            episodeNumber = 1
        )
        fakeRepository.shouldFail = true
        fakeRepository.failureException = Exception("Database error")
        
        // When
        val result = useCase(episode, isWatched = true)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}
