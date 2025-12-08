package org.lanzadera.proyectos.domain.usecase.episodes

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.fakes.FakeWatchedEpisodesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObserveAllWatchedEpisodesUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ObserveAllWatchedEpisodesUseCase
    private lateinit var fakeWatchedEpisodesRepository: FakeWatchedEpisodesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeWatchedEpisodesRepository = FakeWatchedEpisodesRepository()
        useCase = ObserveAllWatchedEpisodesUseCase(fakeWatchedEpisodesRepository)
    }
    
    @Test
    fun `should emit empty list initially`() = runTest {
        // When
        useCase().test {
            // Then
            val episodes = awaitItem()
            assertTrue(episodes.isEmpty())
        }
    }
    
    @Test
    fun `should emit watched episodes when added`() = runTest {
        // When
        useCase().test {
            // Initially empty
            assertTrue(awaitItem().isEmpty())
            
            // Add watched episode
            fakeWatchedEpisodesRepository.markEpisodeAsWatched("456", 1)
            
            // Then - Should emit updated list
            val episodes = awaitItem()
            assertEquals(1, episodes.size)
            assertEquals("456", episodes[0].tvShowId)
            assertEquals(1, episodes[0].episodeNumber)
        }
    }
    
    @Test
    fun `should emit episodes from multiple TV shows`() = runTest {
        // When
        useCase().test {
            // Initially empty
            assertTrue(awaitItem().isEmpty())
            
            // Add episodes from different shows
            fakeWatchedEpisodesRepository.markEpisodeAsWatched("456", 1)
            assertEquals(1, awaitItem().size)
            
            fakeWatchedEpisodesRepository.markEpisodeAsWatched("789", 1)
            
            // Then - Should have episodes from both shows
            val episodes = awaitItem()
            assertEquals(2, episodes.size)
        }
    }
    
    @Test
    fun `should emit updated list when episode unmarked`() = runTest {
        // Given - Episodes from different shows
        fakeWatchedEpisodesRepository.markEpisodeAsWatched("456", 1)
        fakeWatchedEpisodesRepository.markEpisodeAsWatched("789", 1)
        
        // When
        useCase().test {
            // Initial state with 2 episodes
            assertEquals(2, awaitItem().size)
            
            // Remove entire show
            fakeWatchedEpisodesRepository.deleteAllForTvShow("456")
            
            // Then - Should emit updated list with 1 episode
            val episodes = awaitItem()
            assertEquals(1, episodes.size)
            assertEquals("789", episodes[0].tvShowId)
        }
    }
}
