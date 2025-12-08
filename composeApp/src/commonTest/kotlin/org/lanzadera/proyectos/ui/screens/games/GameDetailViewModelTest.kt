package org.lanzadera.proyectos.ui.screens.games

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.usecase.games.GetGameDetailsUseCase
import org.lanzadera.proyectos.fakes.FakeGameRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class GameDetailViewModelTest {

    private lateinit var viewModel: GameDetailViewModel
    private lateinit var fakeGameRepository: FakeGameRepository
    private lateinit var getGameDetailsUseCase: GetGameDetailsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeGameRepository = FakeGameRepository()
        getGameDetailsUseCase = GetGameDetailsUseCase(fakeGameRepository)
        viewModel = GameDetailViewModel(getGameDetailsUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is null`() = runTest {
        viewModel.gameDetails.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `loadGameDetails updates state on success`() = runTest {
        // Given
        val expectedGame = createTestGame(id = 123, name = "Test Game")
        fakeGameRepository.setGameDetails(expectedGame)

        // When
        viewModel.loadGameDetails(123)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.gameDetails.test {
            assertEquals(expectedGame, awaitItem())
        }
    }

    @Test
    fun `loadGameDetails handles null result`() = runTest {
        // Given
        fakeGameRepository.setGameDetails(null)

        // When
        viewModel.loadGameDetails(123)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.gameDetails.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `loadGameDetails with different game IDs updates state correctly`() = runTest {
        // Given
        val game1 = createTestGame(id = 1, name = "Game 1")
        val game2 = createTestGame(id = 2, name = "Game 2")

        // When - Load first game
        fakeGameRepository.setGameDetails(game1)
        viewModel.loadGameDetails(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.gameDetails.test {
            assertEquals(game1, awaitItem())
        }

        // When - Load second game
        fakeGameRepository.setGameDetails(game2)
        viewModel.loadGameDetails(2)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.gameDetails.test {
            assertEquals(game2, awaitItem())
        }
    }

    // Helper function
    private fun createTestGame(id: Int, name: String) = Game(
        id = id,
        name = name,
        summary = null,
        storyline = null,
        rating = null,
        ratingCount = null,
        genres = null,
        platforms = null,
        releaseDates = null,
        cover = null,
        screenshots = null,
        developers = null,
        publishers = null,
        keywords = null,
        involvedCompanies = null,
        artworks = null,
        websites = null,
        gameEngines = null,
        gameModes = null,
        popularity = null,
        slug = null,
        url = null,
        status = null,
        firstReleaseDate = null,
        hype = null,
        updatedAt = null,
        createdAt = null
    )
}
