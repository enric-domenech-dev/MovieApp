package org.lanzadera.proyectos.base

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Example test demonstrating how to use the base test classes.
 */
class BaseTestExample : UseCaseTest() {
    
    @BeforeTest
    override fun setup() {
        super.setup()
    }
    
    @Test
    fun `testData provides valid sample favorites`() {
        val movie = TestData.testFavoriteMovie
        
        assertNotNull(movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals(FavoriteType.MOVIE, movie.type)
    }
    
    @Test
    fun `testData factory methods create custom instances`() {
        val customFavorite = TestData.createFavoriteItem(
            id = "999",
            title = "Custom Item",
            type = FavoriteType.GAME
        )
        
        assertEquals("999", customFavorite.id)
        assertEquals("Custom Item", customFavorite.title)
        assertEquals(FavoriteType.GAME, customFavorite.type)
    }
    
    @Test
    fun `testDispatcher is available for coroutine tests`() = runTest {
        assertNotNull(testDispatcher)
    }
}
