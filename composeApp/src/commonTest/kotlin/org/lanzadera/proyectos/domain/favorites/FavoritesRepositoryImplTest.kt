package org.lanzadera.proyectos.domain.favorites

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.data.repository.FavoritesRepositoryImpl
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesRepositoryImplTest {
    private val dataSource = FakeFavoritesLocalDataSource()
    private val repository = FavoritesRepositoryImpl(dataSource)

    @Test
    fun `toggleFavorite adds and removes`() = runTest {
        val item = FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie")

        repository.toggleFavorite(item)
        assertEquals(1, repository.favorites.first().size)

        repository.toggleFavorite(item)
        assertTrue(repository.favorites.first().isEmpty())
    }

    @Test
    fun `syncFavorites replaces storage`() = runTest {
        val items = listOf(
            FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie"),
            FavoriteItem(id = "2", type = FavoriteType.TV_SHOW, title = "Series")
        )

        repository.syncFavorites(items)
        val stored = repository.favorites.first()
        assertEquals(items.size, stored.size)
        assertEquals(items.map { it.id }.toSet(), stored.map { it.id }.toSet())
    }
}
