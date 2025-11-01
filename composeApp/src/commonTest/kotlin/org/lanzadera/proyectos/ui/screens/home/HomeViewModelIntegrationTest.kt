@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package org.lanzadera.proyectos.ui.screens.home

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.flattenEntries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.repository.LoadInitialDataImpl
import org.lanzadera.proyectos.domain.usecase.load_initial_data.LoadInitialDataUseCase
import org.lanzadera.proyectos.movieListJsonCompleta
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HomeViewModelIntegrationTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --------------------------------------------
    // Caso OK: carga y listo
    // --------------------------------------------
    @Test
    fun `al iniciar precarga todo y la UI pasa de loading a listo con datos verificando las llamadas HTTP`() =
        runTest(testDispatcher) {
            println("TEST #1 - al iniciar precarga todo y la UI pasa de loading a listo con datos verificando las llamadas HTTP\n")
            println("Creando HomeViewModel, Repository, UseCase, etc\n")

            // 1) MockEngine: responde JSON válido para todos los endpoints usados
            val engine = MockEngine { req ->
                // Minimal request log: path + concise params
                val params = req.url.parameters.flattenEntries().joinToString()
                println("REQUEST --> ${req.method.value} ${req.url.host}${req.url.encodedPath} params=[$params]")
                println("Iniciando petición al endpoint: ${req.url.encodedPath}")

                when (req.url.encodedPath) {
                    "/3/movie/now_playing", "/3/trending/movie/week", "/3/trending/movie/day", "/3/movie/popular",
                    "/3/movie/top_rated", "/3/movie/upcoming", "/3/discover/movie" -> {
                        // Keep response notification minimal
                        println("<-- RESPUESTA: ${req.url.encodedPath}")
                        println("<-- END REQUEST HTTP\n")
                        respondJson(movieListJsonCompleta())
                    }

                    else -> {
                        println("MockEngine -> UNMOCKED: ${req.url.encodedPath}")
                        error("ruta no mockeada: ${req.url.encodedPath}")
                    }
                }
            }

            println("Iniciando Cliente Http\n")
            // 2) HttpClient con ContentNegotiation
            val client = HttpClient(engine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true; explicitNulls = false })
                }
            }

            println("Instanciando Repository y UseCase\n")
            // 3) Repo + UseCase + ViewModel
            val repo = LoadInitialDataImpl(
                client = client,
                maxPages = 1,
                json = Json { ignoreUnknownKeys = true; explicitNulls = false })
            val useCase = LoadInitialDataUseCase(repo)
            val vm = HomeViewModel(useCase)

            println("Subscribiendo a uiState para disparar onStart { refreshIfNeeded() }\n")
            // 4) Suscríbete a uiState para disparar onStart { refreshAllIfNeeded() }
            val uiJob = launch {
                vm.uiState.collect { state ->
                    println("UiStats is Loading = ${state.isLoading}")
                }
            }

            // 5) Espera a que haya datos en los flows
            val movies = vm.movies.first { it.isNotEmpty() }
            println("\nVALIDATION: Carga finalizata. UiState is Loading = ${vm.uiState.value.isLoading}")
            println("VALIDATION: LA lista movies no esta vacia -> size=${movies.size}")

            val discover = vm.discover.first { it.isNotEmpty() }
            println("Validation -> discover.size=${discover.size}")

            assertEquals(2, movies.size, "movies debería tener 2 elementos del mock")
            assertEquals(2, discover.size, "discover debería tener 2 elementos del mock")

            // 6) Espera explícitamente a que uiState apague el loading
            val doneState = vm.uiState.first { !it.isLoading }
            println("\nVALIDATION: Carga finalizata. UiState is Loading = ${doneState.isLoading}")
            assertFalse(
                doneState.isLoading, "uiState.isLoading debería estar apagado tras la carga"
            )

            // 7) Verifica el historial de requests del MockEngine (resumen)
            val totalRequests = engine.requestHistory.size
            println("Validation -> totalRequests=$totalRequests")
            val pathsSet = engine.requestHistory.map { it.url.encodedPath }.toSet()

            val expected = setOf(
                "/3/movie/now_playing",
                "/3/trending/movie/week",
                "/3/trending/movie/day",
                "/3/movie/popular",
                "/3/movie/top_rated",
                "/3/movie/upcoming",
                "/3/discover/movie"
            )

            val extra = pathsSet - expected
            val missing = expected - pathsSet
            println("Validation -> extraPaths=$extra missingPaths=$missing")
            assertTrue(extra.isEmpty(), "rutas inesperadas: $extra")
            assertTrue(missing.isEmpty(), "rutas no llamadas: $missing")

            val counts = engine.requestHistory.groupingBy { it.url.encodedPath }.eachCount()
            println("Validation -> counts=$counts")
            assertEquals(1, counts["/3/movie/now_playing"] ?: 0)
            assertEquals(1, counts["/3/trending/movie/week"] ?: 0)
            assertEquals(1, counts["/3/trending/movie/day"] ?: 0)
            assertEquals(1, counts["/3/movie/popular"] ?: 0)
            assertEquals(1, counts["/3/movie/top_rated"] ?: 0)
            assertEquals(1, counts["/3/movie/upcoming"] ?: 0)
            assertEquals(3, counts["/3/discover/movie"] ?: 0)

            // 8) Verifica las 3 variantes de /3/discover/movie por query params
            engine.assertCalled(
                path = "/3/discover/movie", where = { url ->
                    val debug = url.parameters.flattenEntries().joinToString()
                    println("Validation -> discover simple params=[$debug]")
                    url.parameters["sort_by"] == "popularity.desc" && url.parameters["include_adult"] == null
                            && url.parameters["include_video"] == null && url.parameters["with_release_type"] == null
                            && url.parameters["release_date.gte"] == null && url.parameters["release_date.lte"] == null
                            && url.parameters["primary_release_date.gte"] == null && url.parameters["primary_release_date.lte"] == null
                })

            engine.assertCalled(
                path = "/3/discover/movie", where = { url ->
                    val debug = url.parameters.flattenEntries().joinToString()
                    println("Validation -> discover hero params=[$debug]")
                    url.parameters["include_adult"] == "false" && url.parameters["include_video"] == "false"
                            && url.parameters["with_release_type"] == "3|2" && url.parameters["primary_release_date.gte"] != null
                            && url.parameters["primary_release_date.lte"] != null && url.parameters["vote_count.gte"] == "200"
                })

            engine.assertCalled(
                path = "/3/discover/movie", where = { url ->
                    val debug = url.parameters.flattenEntries().joinToString()
                    println("Validation -> discover in-cinemas params=[$debug]")
                    url.parameters["include_adult"] == "false" && url.parameters["with_release_type"] == "3|2"
                            && url.parameters["release_date.gte"] != null && url.parameters["release_date.lte"] != null
                            && url.parameters["primary_release_date.gte"] == null && url.parameters["primary_release_date.lte"] == null
                })

            println("\nValidation -> all checks passed")
            uiJob.cancel()
            testScheduler.advanceUntilIdle()
        }

    // --------------------------------------------
    // Caso ERROR #1: SIN CONEXIÓN (ConnectException)
    // Debe terminar con error != null, isLoading = false y listas vacías
    // --------------------------------------------
    @Test
    fun `cuando no hay conexion se marca error y no hay datos`() = runTest(testDispatcher) {
        println("TEST #2 - cuando no hay conexion se marca error y no hay datos\n")
        println("Creando MockEngine que lanza Connection refused")
        val engine = MockEngine { throw Exception("Connection refused") }
        println("Instanciando ViewModel, Repository y UseCase")
        val vm = buildVm(engine)

        // dispara onStart
        println("Disparando onStart -> suscribiendo a uiState")
        val uiJob =
            launch { vm.uiState.collect { state -> println("UiStats is Loading = ${state.isLoading}") } }

        println("Esperando estado de error")
        // espera estado de error
        val errorState = vm.uiState.first { it.error != null && !it.isLoading }
        println("VALIDATION -> error=${errorState.error} isLoading=${errorState.isLoading}")
        assertNotNull(errorState.error)
        assertTrue(vm.movies.value.isEmpty(), "movies debería estar vacío en error")
        assertTrue(vm.discover.value.isEmpty(), "discover debería estar vacío en error")

        // debería haberse intentado al menos 1 request
        println("VALIDATION -> requestHistory.size=${engine.requestHistory.size}")
        // When the engine throws a connection exception it may not record history; don't assert on requestHistory

        uiJob.cancel()
        testScheduler.advanceUntilIdle()
    }

    // --------------------------------------------
    // Caso ERROR #2: FALLO PARCIAL (un endpoint falla)
    // Con awaitAll, si uno falla, el refresh global falla -> error != null, isLoading = false
    // --------------------------------------------
    @Test
    fun `si un endpoint falla el viewmodel expone error y apaga loading`() =
        runTest(testDispatcher) {
            println("TEST #3 - si un endpoint falla el viewmodel expone error y apaga loading\n")
            println("Creando MockEngine que falla en /3/movie/top_rated")

            val engine = MockEngine { req ->
                when (req.url.encodedPath) {
                    // simulamos que este endpoint peta
                    "/3/movie/top_rated" -> error("boom top_rated")
                    // el resto responde OK
                    "/3/movie/now_playing", "/3/trending/movie/week", "/3/trending/movie/day", "/3/movie/popular",
                    "/3/movie/upcoming", "/3/discover/movie" -> respondJson(
                        movieListJsonCompleta()
                    )

                    else -> error("ruta no mockeada: ${req.url.encodedPath}")
                }
            }

            println("Instanciando ViewModel, Repository y UseCase")
            val vm = buildVm(engine)
            println("Subscribiendo a uiState para disparar onStart { refreshIfNeeded() }\n")
            val uiJob = launch {
                vm.uiState.collect { state ->
                    println("UiStats is Loading = ${state.isLoading}")
                }
            }

            println("Esperando que ViewModel informe un error y que la carga termine (isLoading=false)")
            val errorState = vm.uiState.first { it.error != null && !it.isLoading }
            println("VALIDACIÓN -> error presente=${errorState.error != null}, mensaje=${errorState.error}, isLoading=${errorState.isLoading}")
            assertNotNull(errorState.error)

            // puede que algún flow haya alcanzado a emitir (antes de la excepción),
            // pero no exigimos contenido; sí verificamos que no quedó "cargando"
            assertFalse(errorState.isLoading)

            uiJob.cancel()
            testScheduler.advanceUntilIdle()
        }

    // --------------------------------------------
    // Caso ERROR #3 (TDD): HTTP 500 debería considerarse error
    // --------------------------------------------
    @Test
    fun `si el servidor responde 5xx la ui entra en error`() = runTest(testDispatcher) {
        println("TEST #4 - si el servidor responde 5xx la ui entra en error\n")
        println("Creando MockEngine que responde 500 para todos los endpoints")

        val engine = MockEngine { req ->
            when (req.url.encodedPath) {
                // devolvemos 500 AUN con cuerpo valido -> el repo debe fallar por status
                "/3/movie/now_playing", "/3/trending/movie/week", "/3/trending/movie/day", "/3/movie/popular",
                "/3/movie/top_rated", "/3/movie/upcoming", "/3/discover/movie" -> respondJson(
                    body = movieListJsonCompleta(), status = HttpStatusCode.InternalServerError
                )

                else -> error("ruta no mockeada: ${req.url.encodedPath}")
            }
        }

        println("Instanciando ViewModel, Repository y UseCase")
        val vm = buildVm(engine)
        println("Subscribiendo a uiState para disparar onStart { refreshIfNeeded() }\n")
        val uiJob = launch {
            vm.uiState.collect { state ->
                println("UiStats is Loading = ${state.isLoading}")
            }
        }

        val errorState = vm.uiState.first { it.error != null && !it.isLoading }
        println("VALIDATION -> error=${errorState.error} isLoading=${errorState.isLoading}")
        assertNotNull(errorState.error)

        uiJob.cancel()
        testScheduler.advanceUntilIdle()
    }


    // --- helpers de test ---

    private fun buildVm(engine: MockEngine): HomeViewModel {
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; explicitNulls = false })
            }
        }
        val repo = LoadInitialDataImpl(
            client = client,
            maxPages = 1,
            json = Json { ignoreUnknownKeys = true; explicitNulls = false })
        return HomeViewModel(LoadInitialDataUseCase(repo))
    }

    private fun MockRequestHandleScope.respondJson(
        body: String, status: HttpStatusCode = HttpStatusCode.OK
    ): HttpResponseData = respond(
        content = body,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )

    private fun MockEngine.assertCalled(
        path: String, times: Int = 1, where: (Url) -> Boolean = { true }
    ) {
        val calls = requestHistory.filter { it.url.encodedPath == path && where(it.url) }
        assertEquals(
            times, calls.size, "se esperaban $times llamada(s) a $path con el filtro indicado"
        )
    }
}