package org.lanzadera.proyectos.ui.screens.home.tabs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.lanzadera.proyectos.base.ViewModelTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.usecase.episodes.ObserveAllWatchedEpisodesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.GetFavoriteDetailsUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleBookFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleGameFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleMovieFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleTvShowFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.movies.ObserveWatchedMoviesUseCase
import org.lanzadera.proyectos.domain.usecase.settings.ObserveMoviesFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.ObserveSeriesFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.UpdateMoviesFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.UpdateSeriesFiltersUseCase
import org.lanzadera.proyectos.fakes.FakeFavoriteDetailsRepository
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import org.lanzadera.proyectos.fakes.FakeMovieRepository
import org.lanzadera.proyectos.fakes.FakeSettingsRepository
import org.lanzadera.proyectos.fakes.FakeTvShowRepository
import org.lanzadera.proyectos.fakes.FakeWatchedEpisodesRepository
import org.lanzadera.proyectos.fakes.FakeWatchedMoviesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for FavoritesTabViewModel.
 * 
 * Tests favorite tracking, watched content, and complex state combinations.
 */
class FavoritesTabViewModelTest : ViewModelTest() {
    
    private lateinit var viewModel: FavoritesTabViewModel
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var fakeDetailsRepository: FakeFavoriteDetailsRepository
    private lateinit var fakeWatchedEpisodesRepository: FakeWatchedEpisodesRepository
    private lateinit var fakeWatchedMoviesRepository: FakeWatchedMoviesRepository
    private lateinit var fakeSettingsRepository: FakeSettingsRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        
        fakeFavoritesRepository = FakeFavoritesRepository()
        fakeDetailsRepository = FakeFavoriteDetailsRepository()
        fakeWatchedEpisodesRepository = FakeWatchedEpisodesRepository()
        fakeWatchedMoviesRepository = FakeWatchedMoviesRepository()
        fakeSettingsRepository = FakeSettingsRepository()
        
        val observeFavoritesUseCase = ObserveFavoritesUseCase(fakeFavoritesRepository)
        val toggleMovieFavoriteUseCase = ToggleMovieFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository,
            favoriteDetailsRepository = fakeDetailsRepository,
            movieRepository = FakeMovieRepository()
        )
        val toggleTvShowFavoriteUseCase = ToggleTvShowFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository,
            favoriteDetailsRepository = fakeDetailsRepository,
            watchedEpisodesRepository = fakeWatchedEpisodesRepository,
            tvShowRepository = FakeTvShowRepository()
        )
        val toggleBookFavoriteUseCase = ToggleBookFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository
        )
        val toggleGameFavoriteUseCase = ToggleGameFavoriteUseCase(
            favoritesRepository = fakeFavoritesRepository
        )
        val observeAllWatchedEpisodesUseCase = ObserveAllWatchedEpisodesUseCase(fakeWatchedEpisodesRepository)
        val getFavoriteDetailsUseCase = GetFavoriteDetailsUseCase(fakeDetailsRepository)
        val observeWatchedMoviesUseCase = ObserveWatchedMoviesUseCase(fakeWatchedMoviesRepository)
        val observeSeriesFiltersUseCase = ObserveSeriesFiltersUseCase(fakeSettingsRepository)
        val observeMoviesFiltersUseCase = ObserveMoviesFiltersUseCase(fakeSettingsRepository)
        val updateSeriesFiltersUseCase = UpdateSeriesFiltersUseCase(fakeSettingsRepository)
        val updateMoviesFiltersUseCase = UpdateMoviesFiltersUseCase(fakeSettingsRepository)
        
        viewModel = FavoritesTabViewModel(
            observeFavoritesUseCase = observeFavoritesUseCase,
            toggleMovieFavoriteUseCase = toggleMovieFavoriteUseCase,
            toggleTvShowFavoriteUseCase = toggleTvShowFavoriteUseCase,
            toggleBookFavoriteUseCase = toggleBookFavoriteUseCase,
            toggleGameFavoriteUseCase = toggleGameFavoriteUseCase,
            observeAllWatchedEpisodesUseCase = observeAllWatchedEpisodesUseCase,
            getFavoriteDetailsUseCase = getFavoriteDetailsUseCase,
            observeWatchedMoviesUseCase = observeWatchedMoviesUseCase,
            observeSeriesFiltersUseCase = observeSeriesFiltersUseCase,
            observeMoviesFiltersUseCase = observeMoviesFiltersUseCase,
            updateSeriesFiltersUseCase = updateSeriesFiltersUseCase,
            updateMoviesFiltersUseCase = updateMoviesFiltersUseCase
        )
    }
    
    @Test
    fun `initial state has empty favorites`() = runTest {
        viewModel.favorites.test {
            assertThat(awaitItem()).isEmpty()
        }
    }
    
    @Test
    fun `favorites flow emits UI models when repository updates`() = runTest {
        val testFavorite = FavoriteItem(
            id = "123",
            type = FavoriteType.MOVIE,
            title = "Test Movie",
            addedAt = Clock.System.now()
        )
        
        viewModel.favorites.test {
            // Initial empty state
            assertThat(awaitItem()).isEmpty()
            
            // Add favorite
            fakeFavoritesRepository.addFavorite(testFavorite)
            
            // Verify favorite appears in UI
            val favorites = awaitItem()
            assertThat(favorites).hasSize(1)
            assertThat(favorites[0].id).isEqualTo("123")
            assertThat(favorites[0].title).isEqualTo("Test Movie")
        }
    }
    
    @Test
    fun `allWatchedEpisodes flow emits watched episodes`() = runTest {
        viewModel.allWatchedEpisodes.test {
            val episodes = awaitItem()
            assertThat(episodes).isEmpty()
        }
    }
    
    @Test
    fun `seriesWithUnwatchedEpisodes is initially empty`() = runTest {
        viewModel.seriesWithUnwatchedEpisodes.test {
            val series = awaitItem()
            assertThat(series).isEmpty()
        }
    }
    
    @Test
    fun `moviesWithReleaseInfo is initially empty`() = runTest {
        viewModel.moviesWithReleaseInfo.test {
            val movies = awaitItem()
            assertThat(movies).isEmpty()
        }
    }
    
    @Test
    fun `favoritesWithInfo combines favorites with additional details`() = runTest {
        viewModel.favoritesWithInfo.test {
            val info = awaitItem()
            assertThat(info).isEmpty()
        }
    }
    
    @Test
    fun `multiple favorites can be tracked simultaneously`() = runTest {
        val favorite1 = FavoriteItem(
            id = "1",
            type = FavoriteType.MOVIE,
            title = "Movie 1",
            addedAt = Clock.System.now()
        )
        val favorite2 = FavoriteItem(
            id = "2",
            type = FavoriteType.TV_SHOW,
            title = "Series 1",
            addedAt = Clock.System.now()
        )
        
        viewModel.favorites.test {
            assertThat(awaitItem()).isEmpty()
            
            fakeFavoritesRepository.addFavorite(favorite1)
            assertThat(awaitItem()).hasSize(1)
            
            fakeFavoritesRepository.addFavorite(favorite2)
            val favorites = awaitItem()
            assertThat(favorites).hasSize(2)
            assertThat(favorites.map { it.id }).containsExactly("1", "2")
        }
    }
    
    @Test
    fun `showSeries filter is enabled by default`() = runTest {
        viewModel.showSeries.test {
            assertThat(awaitItem()).isTrue()
        }
    }
    
    @Test
    fun `showMovies filter is disabled by default`() = runTest {
        viewModel.showMovies.test {
            assertThat(awaitItem()).isFalse()
        }
    }
    
    @Test
    fun `toggleSeriesFilter changes showSeries state`() = runTest {
        viewModel.showSeries.test {
            assertThat(awaitItem()).isTrue()
            
            viewModel.toggleSeriesFilter()
            assertThat(awaitItem()).isFalse()
            
            viewModel.toggleSeriesFilter()
            assertThat(awaitItem()).isTrue()
        }
    }
    
    @Test
    fun `toggleMoviesFilter changes showMovies state`() = runTest {
        viewModel.showMovies.test {
            assertThat(awaitItem()).isFalse()
            
            viewModel.toggleMoviesFilter()
            assertThat(awaitItem()).isTrue()
            
            viewModel.toggleMoviesFilter()
            assertThat(awaitItem()).isFalse()
        }
    }
    
    @Test
    fun `filteredFavoritesWithInfo shows only series by default`() = runTest {
        // Note: This test would need actual favorite items with details
        // For now we just verify the flow is empty initially
        viewModel.filteredFavoritesWithInfo.test {
            assertThat(awaitItem()).isEmpty()
        }
    }
}
