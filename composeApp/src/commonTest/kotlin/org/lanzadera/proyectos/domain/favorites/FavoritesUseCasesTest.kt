package org.lanzadera.proyectos.domain.favorites

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.SyncFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleFavoriteUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesUseCasesTest {
    private val repository = FakeFavoritesRepository()
    private val observe = ObserveFavoritesUseCase(repository)
    private val toggle = ToggleFavoriteUseCase(repository)
    private val sync = SyncFavoritesUseCase(repository)

    @Test
    fun `toggle adds and removes favorites`() = runTest {
        val item = FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie")

        toggle(item)
        val firstEmission = observe().valueOrEmpty()
        assertEquals(1, firstEmission.size)

        toggle(item)
        val secondEmission = observe().valueOrEmpty()
        assertTrue(secondEmission.isEmpty())
    }

    @Test
    fun `sync replaces favorites`() = runTest {
        val items = listOf(
            FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "A"),
            FavoriteItem(id = "2", type = FavoriteType.TV_SHOW, title = "B")
        )

        sync(items)
        val emission = observe().valueOrEmpty()
        assertEquals(items, emission)
    }

    private suspend fun Flow<List<FavoriteItem>>.valueOrEmpty(): List<FavoriteItem> = first()
}
