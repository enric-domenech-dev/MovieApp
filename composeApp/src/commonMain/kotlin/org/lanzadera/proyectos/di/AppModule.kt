package org.lanzadera.proyectos.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.lanzadera.proyectos.BuildConfig
import org.lanzadera.proyectos.data.authentication.IGDBAuthManager
import org.lanzadera.proyectos.data.datasource.FavoritesLocalDataSource
import org.lanzadera.proyectos.data.datasource.WatchedEpisodesDataSource
import org.lanzadera.proyectos.data.datasource.createFavoriteDetailsRepository
import org.lanzadera.proyectos.data.datasource.createFavoritesLocalDataSource
import org.lanzadera.proyectos.data.datasource.createWatchedEpisodesDataSource
import org.lanzadera.proyectos.data.datasource.createWatchedMoviesDataSource
import org.lanzadera.proyectos.data.repository.BooksRepositoryImpl
import org.lanzadera.proyectos.data.repository.FavoritesRepositoryImpl
import org.lanzadera.proyectos.data.repository.GameRepositoryImpl
import org.lanzadera.proyectos.data.repository.LoadInitialDataImpl
import org.lanzadera.proyectos.data.repository.MovieRepositoryImpl
import org.lanzadera.proyectos.data.repository.SearchRepositoryImpl
import org.lanzadera.proyectos.data.repository.TvShowRepositoryImpl
import org.lanzadera.proyectos.data.repository.WatchedEpisodesRepositoryImpl
import org.lanzadera.proyectos.data.repository.WatchedMoviesRepositoryImpl
import org.lanzadera.proyectos.domain.repository.BooksRepository
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.domain.repository.GameRepository
import org.lanzadera.proyectos.domain.repository.LoadInitialData
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.domain.repository.SearchRepository
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository
import org.lanzadera.proyectos.domain.usecase.books.RefreshBooksUseCase
import org.lanzadera.proyectos.domain.usecase.episodes.ObserveWatchedEpisodesUseCase
import org.lanzadera.proyectos.domain.usecase.episodes.ToggleEpisodeWatchedUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.SyncFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.games.GetGameDetailsUseCase
import org.lanzadera.proyectos.domain.usecase.games.RefreshGamesUseCase
import org.lanzadera.proyectos.domain.usecase.load_initial_data.GetInitialDataUseCase
import org.lanzadera.proyectos.domain.usecase.movies.ToggleMovieWatchedUseCase
import org.lanzadera.proyectos.domain.usecase.search.SearchMoviesUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.GetTvShowDetailsUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.RefreshTvShowsUseCase
import org.lanzadera.proyectos.ui.screens.detail.MovieDetailViewModel
import org.lanzadera.proyectos.ui.screens.detail.SeriesDetailViewModel
import org.lanzadera.proyectos.ui.screens.games.GameDetailViewModel
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel
import org.lanzadera.proyectos.ui.screens.search.SearchViewModel
import org.lanzadera.proyectos.ui.screens.splash.SplashViewModel

val appModule = module {
    single(named("apiBearerToken")) { BuildConfig.API_BEARER_TOKEN }
}

// Plugin personalizado para medir el tiempo de respuesta
val LoggingPlugin = createClientPlugin("LoggingPlugin") {
    val json = Json {
        prettyPrint = true
    }
    onRequest { request, _ ->
        val method = request.method.value
        val url = request.url
        println(
            "--> SYNCRO REQUEST $method $url \n " +
                    "---> HEADERS: ${
                        json.encodeToString(
                            MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                            request.headers.entries().associate { it.key to it.value }
                        )
                    } \n ---> BODY: ${
                        json.encodeToString(String.serializer(), request.body.toString())
                    }"
        )
    }
    onResponse { response ->
        val call = response.call
        val request = call.request
        val method = request.method
        val ct = response.headers["Content-Type"] ?: ""
        val url = request.url.toString()
        val startTime = response.requestTime.timestamp
        val endTime = response.responseTime.timestamp
        val elapsed = endTime - startTime
        val body = response.bodyAsText()
        println("<-- END REQUEST ${method.value} $url (${elapsed}ms)")
        println("<-- SYNCRO RESPONSE CODE ${response.status}")
        runCatching {
            if (ct.contains("application/json", ignoreCase = true))
                println("<-- RESPONSE BODY (json): ${json.encodeToString(JsonElement.serializer(), Json.parseToJsonElement(body))}")
            else
                println("<-- RESPONSE BODY (text): $body")
        }.getOrElse {
            println("<-- RESPONSE BODY (raw): $body") // no bloquees la llamada por el logger
        }
    }
}

val dataModule = module {

    // Json
    single {
        Json {
            ignoreUnknownKeys =
                true // Ignora claves desconocidas en el JSON recibido, evitando errores por campos extra
            prettyPrint =
                true // Formatea el JSON de salida para que sea legible (solo útil para debug/logs)
            isLenient =
                true // Permite que el parser sea más tolerante con el formato del JSON (por ejemplo, comas finales)
            coerceInputValues =
                true // Convierte valores de entrada que no coinciden exactamente con el tipo esperado
            encodeDefaults = true // Serializa valores nulos explícitamente en el JSON de salida
        }
    }

    // HTTPS Client for TMDB (existing)
    single {
        HttpClient {
            expectSuccess = true
            install(HttpTimeout)
            install(ContentNegotiation) { json(get()) }
//            install(Logging) { level = LogLevel.ALL }
            install(LoggingPlugin)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                    headers.append("accept", "application/json")
                    headers.append("Authorization", "Bearer " + BuildConfig.API_BEARER_TOKEN)
                }
            }
        }
    }

    // HTTPS Client for Google Books API - named binding
    single(named("googleBooksClient")) {
        HttpClient {
            expectSuccess = true
            install(HttpTimeout)
            install(ContentNegotiation) { json(get()) }
            install(LoggingPlugin)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.googleapis.com"
                    headers.append("accept", "application/json")
                }
            }
        }
    }

    // API key for Google Books (optional). Replace value via DI or update this binding to use BuildConfig when you add the key.
    single(named("googleBooksApiKey")) { "" }

    // HTTPS Client for IGDB API - named binding
    // Note: expectSuccess = false because headers (Client-ID, Authorization) are added per-request
    single(named("igdbClient")) {
        HttpClient {
            expectSuccess = false
            install(HttpTimeout)
            install(ContentNegotiation) { json(get()) }
            install(LoggingPlugin)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.igdb.com"
                    headers.append("accept", "application/json")
                }
            }
        }
    }

    // IGDB Auth Manager
    single {
        IGDBAuthManager(
            clientId = BuildConfig.IGDB_CLIENT_ID,
            clientSecret = BuildConfig.IGDB_CLIENT_SECRET,
            httpClient = get(named("igdbClient")),
            json = get()
        )
    }

    // Favorites persistence
    single<FavoritesLocalDataSource> { createFavoritesLocalDataSource() }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }

    // Favorite details with full info (Room)
    single<FavoriteDetailsRepository> { createFavoriteDetailsRepository() }

    // Watched episodes persistence
    single<WatchedEpisodesDataSource> { createWatchedEpisodesDataSource() }
    single<WatchedEpisodesRepository> { WatchedEpisodesRepositoryImpl(get()) }

    // Watched movies persistence - need provider
    single<WatchedMoviesRepository> { WatchedMoviesRepositoryImpl(createWatchedMoviesDataSource()) }
}

val viewModelsModule = module {

    // UseCases
    single { GetInitialDataUseCase(get()) }
    single { RefreshBooksUseCase(get()) }
    single { RefreshTvShowsUseCase(get()) }
    single { GetTvShowDetailsUseCase(get()) }
    single { SearchMoviesUseCase(get()) }
    single { RefreshGamesUseCase(get()) }
    single { GetGameDetailsUseCase(get()) }
    single { ObserveFavoritesUseCase(get()) }
    single { ToggleFavoriteUseCase(get(), get(), get(), get(), get()) }
    single { SyncFavoritesUseCase(get()) }
    single { ObserveWatchedEpisodesUseCase(get()) }
    single { ToggleEpisodeWatchedUseCase(get()) }
    single { ToggleMovieWatchedUseCase(get()) }

    // Repositories
    single<LoadInitialData> { LoadInitialDataImpl(get(), 5, get()) }
    single<BooksRepository> {
        BooksRepositoryImpl(
            get(named("googleBooksClient")),
            get(),
            get(named("googleBooksApiKey"))
        )
    }
    single<TvShowRepository> { TvShowRepositoryImpl(get(), 5, get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), 5, get()) }
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    single<GameRepository> { GameRepositoryImpl(get(), get(named("igdbClient")), 5, get()) }

    // ViewModels
    viewModel { SplashViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SeriesDetailViewModel(get(), get(), get(), get(), get()) }
    viewModel { MovieDetailViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { GameDetailViewModel(get()) }
}

val nativeModule: Module = module {}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}