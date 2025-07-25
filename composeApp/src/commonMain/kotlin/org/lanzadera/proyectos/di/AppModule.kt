package org.lanzadera.proyectos.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.lanzadera.proyectos.BuildConfig
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel


val appModule = module {
    single(named("apiBearerToken")) { BuildConfig.API_BEARER_TOKEN }
}

val dataModule = module {

    // Json
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    // HTTPS Client
    single {
        HttpClient {
            install(HttpTimeout)
            install(ContentNegotiation) {
                json(get())
            }
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
}

val viewModelsModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

expect val nativeModule: Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}