package org.lanzadera.proyectos.ui.screens.home

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeViewModelTest {

    private val mockMoviesJson = """
        {
            "page": 1,
            "results": [
                {
                    "id": 1,
                    "title": "Movie 1",
                    "poster_path": "/path1.jpg",
                    "overview": "Overview 1",
                    "release_date": "2023-01-01",
                    "vote_average": 8.5,
                    "vote_count": 100,
                    "popularity": 200.0,
                    "original_language": "en",
                    "original_title": "Original Movie 1",
                    "backdrop_path": "/backdrop1.jpg",
                    "adult": false,
                    "video": false
                }
            ],
            "total_pages": 1,
            "total_results": 1
        }
    """.trimIndent()

    @Test
    fun `initUIState devuelve Success cuando hay peliculas validas`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = mockMoviesJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine)
        val viewModel = HomeViewModel(client, maxPages = 1)

        val state = viewModel.initUIState()

        assertTrue(state is HomeViewModel.UIState.Success)

        assertEquals(1, state.trendingMovies.size)
        assertEquals("Movie 1", state.trendingMovies.first().title)
    }

    @Test
    fun `initUIState devuelve Success cuando la respuesta es vacia`() = runTest {
        val emptyJson = """
            {
                "page": 1,
                "results": [],
                "total_pages": 1,
                "total_results": 0
            }
        """.trimIndent()

        val mockEngine = MockEngine {
            respond(
                content = emptyJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine)
        val viewModel = HomeViewModel(client, maxPages = 1)

        val state = viewModel.initUIState()

        assertTrue(state is HomeViewModel.UIState.Success)
        assertTrue(state.trendingMovies.isEmpty())
    }

    @Test
    fun `initUIState devuelve Error si el servidor falla`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": "Internal Server Error"}""",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine)
        val viewModel = HomeViewModel(client)

        val state = viewModel.initUIState()

        assertTrue(state is HomeViewModel.UIState.Error)
        assertTrue(state.message.contains("500") || state.message.contains("Server Error"))
    }

    @Test
    fun `initUIState devuelve Error si ocurre una excepcion`() = runTest {
        val mockEngine = MockEngine {
            throw IOException("Network failure")
        }

        val client = HttpClient(mockEngine)
        val viewModel = HomeViewModel(client)

        val state = viewModel.initUIState()

        assertTrue(state is HomeViewModel.UIState.Error)
        assertTrue(state.message.contains("Network") || state.message.contains("IOException"))
    }
}
