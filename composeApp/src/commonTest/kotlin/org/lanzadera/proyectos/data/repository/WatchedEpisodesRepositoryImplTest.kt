package org.lanzadera.proyectos.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.fakes.FakeWatchedEpisodesDataSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WatchedEpisodesRepositoryImplTest {
    
    private lateinit var dataSource: FakeWatchedEpisodesDataSource
    private lateinit var repository: WatchedEpisodesRepositoryImpl
    
    @BeforeTest
    fun setup() {
        dataSource = FakeWatchedEpisodesDataSource()
        repository = WatchedEpisodesRepositoryImpl(dataSource)
    }
    
    @Test
    fun `observeAllWatchedEpisodes emits empty list initially`() = runTest {
        repository.observeAllWatchedEpisodes().test {
            val episodes = awaitItem()
            assertTrue(episodes.isEmpty())
        }
    }
    
    @Test
    fun `observeWatchedEpisodes emits empty list for show with no watched episodes`() = runTest {
        repository.observeWatchedEpisodes("show123").test {
            val episodes = awaitItem()
            assertTrue(episodes.isEmpty())
        }
    }
    
    @Test
    fun `toggleEpisodeWatched marks episode as watched when isWatched is true`() = runTest {
        val episode = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        
        repository.observeAllWatchedEpisodes().test {
            // Initial state
            assertEquals(0, awaitItem().size)
            
            // Mark as watched
            repository.toggleEpisodeWatched(episode, true)
            
            // Should emit updated list
            val episodes = awaitItem()
            assertEquals(1, episodes.size)
            assertEquals("show1", episodes[0].tvShowId)
            assertEquals(1, episodes[0].seasonNumber)
            assertEquals(1, episodes[0].episodeNumber)
        }
    }
    
    @Test
    fun `toggleEpisodeWatched marks episode as unwatched when isWatched is false`() = runTest {
        val episode = WatchedEpisode(tvShowId = "show2", seasonNumber = 2, episodeNumber = 5)
        
        repository.observeAllWatchedEpisodes().test {
            assertEquals(0, awaitItem().size)
            
            // Mark as watched
            repository.toggleEpisodeWatched(episode, true)
            assertEquals(1, awaitItem().size)
            
            // Mark as unwatched
            repository.toggleEpisodeWatched(episode, false)
            assertEquals(0, awaitItem().size)
        }
    }
    
    @Test
    fun `observeWatchedEpisodes filters by tvShowId`() = runTest {
        val episode1 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        val episode2 = WatchedEpisode(tvShowId = "show2", seasonNumber = 1, episodeNumber = 1)
        val episode3 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 2)
        
        // Mark episodes as watched
        repository.toggleEpisodeWatched(episode1, true)
        repository.toggleEpisodeWatched(episode2, true)
        repository.toggleEpisodeWatched(episode3, true)
        
        // Observe show1 only
        repository.observeWatchedEpisodes("show1").test {
            val episodes = awaitItem()
            assertEquals(2, episodes.size)
            assertTrue(episodes.all { it.tvShowId == "show1" })
        }
    }
    
    @Test
    fun `observeAllWatchedEpisodes includes episodes from all shows`() = runTest {
        val episode1 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        val episode2 = WatchedEpisode(tvShowId = "show2", seasonNumber = 1, episodeNumber = 1)
        val episode3 = WatchedEpisode(tvShowId = "show3", seasonNumber = 2, episodeNumber = 3)
        
        repository.observeAllWatchedEpisodes().test {
            assertEquals(0, awaitItem().size)
            
            repository.toggleEpisodeWatched(episode1, true)
            assertEquals(1, awaitItem().size)
            
            repository.toggleEpisodeWatched(episode2, true)
            assertEquals(2, awaitItem().size)
            
            repository.toggleEpisodeWatched(episode3, true)
            val episodes = awaitItem()
            assertEquals(3, episodes.size)
        }
    }
    
    @Test
    fun `deleteAllForTvShow removes all episodes for specific show`() = runTest {
        val episode1 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        val episode2 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 2)
        val episode3 = WatchedEpisode(tvShowId = "show2", seasonNumber = 1, episodeNumber = 1)
        
        repository.observeAllWatchedEpisodes().test {
            assertEquals(0, awaitItem().size)
            
            // Add episodes from two shows
            repository.toggleEpisodeWatched(episode1, true)
            repository.toggleEpisodeWatched(episode2, true)
            repository.toggleEpisodeWatched(episode3, true)
            
            // Skip intermediate emissions
            skipItems(2)
            assertEquals(3, awaitItem().size)
            
            // Delete all for show1
            repository.deleteAllForTvShow("show1")
            
            // Should only have show2 episodes left
            val remaining = awaitItem()
            assertEquals(1, remaining.size)
            assertEquals("show2", remaining[0].tvShowId)
        }
    }
    
    @Test
    fun `multiple episodes from same show can be tracked`() = runTest {
        val episode1 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        val episode2 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 2)
        val episode3 = WatchedEpisode(tvShowId = "show1", seasonNumber = 2, episodeNumber = 1)
        
        repository.observeWatchedEpisodes("show1").test {
            assertEquals(0, awaitItem().size)
            
            repository.toggleEpisodeWatched(episode1, true)
            assertEquals(1, awaitItem().size)
            
            repository.toggleEpisodeWatched(episode2, true)
            assertEquals(2, awaitItem().size)
            
            repository.toggleEpisodeWatched(episode3, true)
            val episodes = awaitItem()
            assertEquals(3, episodes.size)
            
            // Verify episode IDs are unique
            val ids = episodes.map { it.id }
            assertEquals(3, ids.distinct().size)
        }
    }
    
    @Test
    fun `toggling same episode multiple times works correctly`() = runTest {
        val episode = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        
        repository.observeWatchedEpisodes("show1").test {
            assertEquals(0, awaitItem().size)
            
            // Mark as watched
            repository.toggleEpisodeWatched(episode, true)
            assertEquals(1, awaitItem().size)
            
            // Mark as unwatched
            repository.toggleEpisodeWatched(episode, false)
            assertEquals(0, awaitItem().size)
            
            // Mark as watched again
            repository.toggleEpisodeWatched(episode, true)
            val episodes = awaitItem()
            assertEquals(1, episodes.size)
            assertEquals("show1-S1E1", episodes[0].id)
        }
    }
    
    @Test
    fun `episode id format is correct`() = runTest {
        val episode = WatchedEpisode(tvShowId = "12345", seasonNumber = 3, episodeNumber = 15)
        
        repository.toggleEpisodeWatched(episode, true)
        
        repository.observeAllWatchedEpisodes().test {
            val episodes = awaitItem()
            assertEquals("12345-S3E15", episodes[0].id)
        }
    }
    
    @Test
    fun `observeWatchedEpisodes reflects current state when collected late`() = runTest {
        val episode1 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 1)
        val episode2 = WatchedEpisode(tvShowId = "show1", seasonNumber = 1, episodeNumber = 2)
        
        // Mark episodes before observing
        repository.toggleEpisodeWatched(episode1, true)
        repository.toggleEpisodeWatched(episode2, true)
        
        // Start observing - should get current state
        repository.observeWatchedEpisodes("show1").test {
            val episodes = awaitItem()
            assertEquals(2, episodes.size)
        }
    }
}
