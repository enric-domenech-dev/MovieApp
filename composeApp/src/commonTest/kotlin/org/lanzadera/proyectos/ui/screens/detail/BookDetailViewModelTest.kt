package org.lanzadera.proyectos.ui.screens.detail

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleBookFavoriteUseCase
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BookDetailViewModelTest {

    private lateinit var viewModel: BookDetailViewModel
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var observeFavoritesUseCase: ObserveFavoritesUseCase
    private lateinit var toggleBookFavoriteUseCase: ToggleBookFavoriteUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeFavoritesRepository = FakeFavoritesRepository()
        observeFavoritesUseCase = ObserveFavoritesUseCase(fakeFavoritesRepository)
        toggleBookFavoriteUseCase = ToggleBookFavoriteUseCase(fakeFavoritesRepository)
        viewModel = BookDetailViewModel(observeFavoritesUseCase, toggleBookFavoriteUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial favorites state is empty`() = runTest {
        viewModel.favorites.test {
            assertTrue(awaitItem().isEmpty())
        }
    }

    @Test
    fun `toggleFavorite adds book to favorites when not favorited`() = runTest {
        // Given
        val book = createTestBook(id = "1", title = "Test Book")

        // When
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.favorites.test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("1", favorites.first().id)
            assertEquals(FavoriteType.BOOK, favorites.first().type)
            assertEquals("Test Book", favorites.first().title)
        }
    }

    @Test
    fun `toggleFavorite removes book from favorites when already favorited`() = runTest {
        // Given
        val book = createTestBook(id = "1", title = "Test Book")
        val favoriteItem = FavoriteItem(
            id = "1",
            type = FavoriteType.BOOK,
            title = "Test Book",
            posterUrl = book.thumbnail,
            overview = book.description
        )
        fakeFavoritesRepository.addFavorite(favoriteItem)

        // When
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.favorites.test {
            assertTrue(awaitItem().isEmpty())
        }
    }

    @Test
    fun `toggleFavorite does not add book when id is null`() = runTest {
        // Given
        val book = createTestBook(id = null, title = "Test Book")

        // When
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.favorites.test {
            assertTrue(awaitItem().isEmpty())
        }
    }

    @Test
    fun `toggleFavorite uses empty string for title when title is null`() = runTest {
        // Given
        val book = createTestBook(id = "1", title = null)

        // When
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.favorites.test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("", favorites.first().title)
        }
    }

    @Test
    fun `favorites state updates when book is toggled multiple times`() = runTest {
        // Given
        val book = createTestBook(id = "1", title = "Test Book")

        // When - Add
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Added
        viewModel.favorites.test {
            assertFalse(awaitItem().isEmpty())
        }

        // When - Remove
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Removed
        viewModel.favorites.test {
            assertTrue(awaitItem().isEmpty())
        }

        // When - Add again
        viewModel.toggleFavorite(book)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Added again
        viewModel.favorites.test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("1", favorites.first().id)
        }
    }

    @Test
    fun `multiple different books can be favorited`() = runTest {
        // Given
        val book1 = createTestBook(id = "1", title = "Book 1")
        val book2 = createTestBook(id = "2", title = "Book 2")

        // When
        viewModel.toggleFavorite(book1)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.toggleFavorite(book2)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.favorites.test {
            val favorites = awaitItem()
            assertEquals(2, favorites.size)
            assertTrue(favorites.any { it.id == "1" && it.title == "Book 1" })
            assertTrue(favorites.any { it.id == "2" && it.title == "Book 2" })
        }
    }

    // Helper function
    private fun createTestBook(
        id: String?,
        title: String?,
        authors: List<String>? = listOf("Test Author"),
        description: String? = "Test description",
        thumbnail: String? = "http://test.com/image.jpg",
        publishedDate: String? = "2024-01-01"
    ) = Book(
        id = id,
        title = title,
        authors = authors,
        description = description,
        thumbnail = thumbnail,
        publishedDate = publishedDate
    )
}
