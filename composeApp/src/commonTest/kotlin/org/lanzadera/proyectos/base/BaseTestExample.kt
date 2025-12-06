package org.lanzadera.proyectos.base

import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Example test demonstrating how to use the base test classes.
 * 
 * This test serves as documentation and can be used as a template.
 */
class BaseTestExample : UseCaseTest() {
    
    @BeforeTest
    override fun setup() {
        super.setup()
        // Your setup code here
    }
    
    @Test
    fun `testData provides valid sample movies`() {
        // Given
        val movie = TestData.testMovie
        
        // Then
        assertNotNull(movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals(8.5, movie.voteAverage)
    }
    
    @Test
    fun `testData provides valid sample tv shows`() {
        // Given
        val tvShow = TestData.testTvShow
        
        // Then
        assertNotNull(tvShow.id)
        assertEquals("Test TV Show", tvShow.name)
        assertEquals(8.9, tvShow.voteAverage)
    }
    
    @Test
    fun `testData factory methods create custom instances`() {
        // Given & When
        val customMovie = TestData.createMovie(
            id = 999,
            title = "Custom Movie",
            voteAverage = 9.5
        )
        
        // Then
        assertEquals(999, customMovie.id)
        assertEquals("Custom Movie", customMovie.title)
        assertEquals(9.5, customMovie.voteAverage)
    }
    
    @Test
    fun `testDispatcher is available for coroutine tests`() = runTest {
        // The testDispatcher from UseCaseTest is available
        assertNotNull(testDispatcher)
    }
}
