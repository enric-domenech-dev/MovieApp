package org.lanzadera.proyectos.ui.screens.detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.tvshow.Episode
import org.lanzadera.proyectos.domain.models.tvshow.Season
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.usecase.episodes.ObserveWatchedEpisodesUseCase
import org.lanzadera.proyectos.domain.usecase.episodes.ToggleEpisodeWatchedUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleTvShowFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.GetTvShowDetailsUseCase
import org.lanzadera.proyectos.fakes.FakeFavoriteDetailsRepository
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import org.lanzadera.proyectos.fakes.FakeTvShowRepository
import org.lanzadera.proyectos.fakes.FakeWatchedEpisodesRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesDetailViewModelTest {

    private lateinit var viewModel: SeriesDetailViewModel
    private lateinit var fakeTvShowRepository: FakeTvShowRepository
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var fakeFavoriteDetailsRepository: FakeFavoriteDetailsRepository
    private lateinit var fakeWatchedEpisodesRepository: FakeWatchedEpisodesRepository
    private lateinit var getTvShowDetailsUseCase: GetTvShowDetailsUseCase
    private lateinit var observeFavoritesUseCase: ObserveFavoritesUseCase
    private lateinit var toggleTvShowFavoriteUseCase: ToggleTvShowFavoriteUseCase
    private lateinit var observeWatchedEpisodesUseCase: ObserveWatchedEpisodesUseCase
    private lateinit var toggleEpisodeWatchedUseCase: ToggleEpisodeWatchedUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakeTvShowRepository = FakeTvShowRepository()
        fakeFavoritesRepository = FakeFavoritesRepository()
        fakeFavoriteDetailsRepository = FakeFavoriteDetailsRepository()
        fakeWatchedEpisodesRepository = FakeWatchedEpisodesRepository()

        getTvShowDetailsUseCase = GetTvShowDetailsUseCase(fakeTvShowRepository)
        observeFavoritesUseCase = ObserveFavoritesUseCase(fakeFavoritesRepository)
        toggleTvShowFavoriteUseCase = ToggleTvShowFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository,
            favoriteDetailsRepository = fakeFavoriteDetailsRepository,
            watchedEpisodesRepository = fakeWatchedEpisodesRepository,
            tvShowRepository = fakeTvShowRepository
        )
        observeWatchedEpisodesUseCase = ObserveWatchedEpisodesUseCase(fakeWatchedEpisodesRepository)
        toggleEpisodeWatchedUseCase = ToggleEpisodeWatchedUseCase(fakeWatchedEpisodesRepository)

        viewModel = SeriesDetailViewModel(
            observeFavoritesUseCase = observeFavoritesUseCase,
            toggleTvShowFavoriteUseCase = toggleTvShowFavoriteUseCase,
            observeWatchedEpisodesUseCase = observeWatchedEpisodesUseCase,
            toggleEpisodeWatchedUseCase = toggleEpisodeWatchedUseCase,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is null`() = runTest {
        assertNull(viewModel.tvShowDetail.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
        assertTrue(viewModel.watchedEpisodes.value.isEmpty())
    }

    @Test
    fun `loadTvShowDetails updates state on success`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad")
        fakeTvShowRepository.setTvShowDetails(1, tvShow)

        // When
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.tvShowDetail.value)
        assertEquals("Breaking Bad", viewModel.tvShowDetail.value?.name)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadTvShowDetails sets error when result is null`() = runTest {
        // Given
        fakeTvShowRepository.setTvShowDetails(1, null)

        // When
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // Then
        assertNull(viewModel.tvShowDetail.value)
        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.error.value)
        assertEquals("No se pudieron cargar los detalles de la serie", viewModel.error.value)
    }

    @Test
    fun `loadTvShowDetails sets error on repository failure`() = runTest {
        // Given
        fakeTvShowRepository.shouldFail = true

        // When
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // Then
        assertNull(viewModel.tvShowDetail.value)
        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.error.value)
    }

    @Test
    fun `setTvShowDetail updates state and loads full details`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad")
        val fullTvShow = createTestTvShow(id = 1, name = "Breaking Bad", seasons = listOf(
            Season(seasonNumber = 1, episodes = listOf(Episode(id = 1, episodeNumber = 1)))
        ))
        fakeTvShowRepository.setTvShowDetails(1, fullTvShow)

        // When
        viewModel.setTvShowDetail(tvShow)
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.tvShowDetail.value)
        assertEquals("Breaking Bad", viewModel.tvShowDetail.value?.name)
        assertNotNull(viewModel.tvShowDetail.value?.seasons)
    }

    @Test
    fun `toggleFavorite adds TV show to favorites`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad")
        fakeTvShowRepository.setTvShowDetails(1, tvShow)
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // When
        viewModel.toggleFavorite()
        advanceUntilIdle()

        // Then
        val favorites = viewModel.favorites.value
        assertTrue(favorites.any { it.id == "1" && it.type == FavoriteType.TV_SHOW })
    }

    @Test
    fun `toggleFavorite does nothing when tvShow is null`() = runTest {
        // Given - no TV show loaded

        // When
        viewModel.toggleFavorite()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.favorites.value.isEmpty())
    }

    @Test
    fun `toggleEpisodeWatched marks episode as watched`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad", seasons = listOf(
            Season(seasonNumber = 1, episodes = listOf(
                Episode(id = 1, episodeNumber = 1, name = "Pilot")
            ))
        ))
        fakeTvShowRepository.setTvShowDetails(1, tvShow)
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // When
        viewModel.toggleEpisodeWatched(seasonNumber = 1, episodeNumber = 1, isWatched = true)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isEpisodeWatched(1, 1))
    }

    @Test
    fun `toggleEpisodeWatched marks all previous episodes when marking as watched`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad", seasons = listOf(
            Season(seasonNumber = 1, episodes = listOf(
                Episode(id = 1, episodeNumber = 1, name = "Episode 1"),
                Episode(id = 2, episodeNumber = 2, name = "Episode 2"),
                Episode(id = 3, episodeNumber = 3, name = "Episode 3")
            ))
        ))
        fakeTvShowRepository.setTvShowDetails(1, tvShow)
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // When - Mark episode 3 as watched
        viewModel.toggleEpisodeWatched(seasonNumber = 1, episodeNumber = 3, isWatched = true)
        advanceUntilIdle()

        // Then - Episodes 1, 2, 3 should all be marked as watched
        assertTrue(viewModel.isEpisodeWatched(1, 1))
        assertTrue(viewModel.isEpisodeWatched(1, 2))
        assertTrue(viewModel.isEpisodeWatched(1, 3))
    }

    @Test
    fun `toggleEpisodeWatched unmarks episode and all subsequent episodes`() = runTest {
        // Given - All 3 episodes are watched
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad", seasons = listOf(
            Season(seasonNumber = 1, episodes = listOf(
                Episode(id = 1, episodeNumber = 1, name = "Episode 1"),
                Episode(id = 2, episodeNumber = 2, name = "Episode 2"),
                Episode(id = 3, episodeNumber = 3, name = "Episode 3")
            ))
        ))
        fakeTvShowRepository.setTvShowDetails(1, tvShow)
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()
        
        // Mark all episodes as watched
        viewModel.toggleEpisodeWatched(seasonNumber = 1, episodeNumber = 3, isWatched = true)
        advanceUntilIdle()

        // When - Unmark episode 2
        viewModel.toggleEpisodeWatched(seasonNumber = 1, episodeNumber = 2, isWatched = false)
        advanceUntilIdle()

        // Then - Episode 1 should still be watched, but 2 and 3 should be unwatched
        assertTrue(viewModel.isEpisodeWatched(1, 1))
        assertFalse(viewModel.isEpisodeWatched(1, 2))
        assertFalse(viewModel.isEpisodeWatched(1, 3))
    }

    @Test
    fun `toggleEpisodeWatched handles multiple seasons correctly`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad", seasons = listOf(
            Season(seasonNumber = 1, episodes = listOf(
                Episode(id = 1, episodeNumber = 1, name = "S1E1"),
                Episode(id = 2, episodeNumber = 2, name = "S1E2")
            )),
            Season(seasonNumber = 2, episodes = listOf(
                Episode(id = 3, episodeNumber = 1, name = "S2E1"),
                Episode(id = 4, episodeNumber = 2, name = "S2E2")
            ))
        ))
        fakeTvShowRepository.setTvShowDetails(1, tvShow)
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // When - Mark S2E1 as watched (should mark all of S1 and S2E1)
        viewModel.toggleEpisodeWatched(seasonNumber = 2, episodeNumber = 1, isWatched = true)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isEpisodeWatched(1, 1)) // S1E1
        assertTrue(viewModel.isEpisodeWatched(1, 2)) // S1E2
        assertTrue(viewModel.isEpisodeWatched(2, 1)) // S2E1
        assertFalse(viewModel.isEpisodeWatched(2, 2)) // S2E2 should not be watched
    }

    @Test
    fun `isEpisodeWatched returns false for unwatched episode`() = runTest {
        // Given
        val tvShow = createTestTvShow(id = 1, name = "Breaking Bad")
        fakeTvShowRepository.setTvShowDetails(1, tvShow)
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()

        // When/Then
        assertFalse(viewModel.isEpisodeWatched(1, 1))
    }

    @Test
    fun `loading multiple TV shows updates state correctly`() = runTest {
        // Given
        val tvShow1 = createTestTvShow(id = 1, name = "Breaking Bad")
        val tvShow2 = createTestTvShow(id = 2, name = "Better Call Saul")
        fakeTvShowRepository.setTvShowDetails(1, tvShow1)
        fakeTvShowRepository.setTvShowDetails(2, tvShow2)

        // When - Load first TV show
        viewModel.loadTvShowDetails(1)
        advanceUntilIdle()
        assertEquals("Breaking Bad", viewModel.tvShowDetail.value?.name)

        // Then - Load second TV show
        viewModel.loadTvShowDetails(2)
        advanceUntilIdle()
        assertEquals("Better Call Saul", viewModel.tvShowDetail.value?.name)
    }

    // Helper function to create test TV shows
    private fun createTestTvShow(
        id: Int,
        name: String,
        seasons: List<Season>? = null
    ): TvShow {
        return TvShow(
            id = id,
            name = name,
            originalName = name,
            overview = "Test overview",
            posterPath = "/test-poster.jpg",
            backdropPath = "/test-backdrop.jpg",
            voteAverageDouble = 8.5,
            voteCount = 1000,
            popularity = 100.0,
            firstAirDate = "2008-01-20",
            lastAirDate = "2013-09-29",
            status = "Ended",
            type = "Scripted",
            numberOfSeasons = seasons?.size ?: 5,
            numberOfEpisodes = 62,
            seasons = seasons,
            genres = emptyList(),
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            spokenLanguages = emptyList(),
            networks = emptyList(),
            createdBy = emptyList(),
            aggregateCredits = null
        )
    }
}
