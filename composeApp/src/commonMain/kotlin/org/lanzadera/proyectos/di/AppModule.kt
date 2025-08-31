package org.lanzadera.proyectos.di

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.serialization.json.JsonElement
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.dsl.viewModel
import io.ktor.client.statement.bodyAsText
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

import org.koin.dsl.module
import org.lanzadera.proyectos.BuildConfig
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel

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
            "--> REQUEST $method $url \n " +
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
        val url = request.url.toString()
        val startTime = response.requestTime.timestamp
        val endTime = response.responseTime.timestamp
        val elapsed = endTime - startTime
        val body = response.bodyAsText()
        println("<-- END REQUEST ${method.value} $url (${elapsed}ms)")
        println("<-- RESPONSE CODE ${response.status}")
        println(
            "<-- RESPONSE HEADER ${
                json.encodeToString(
                    MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                    request.headers.entries().associate { it.key to it.value }
                )
            } \n ---> RESPONSE BODY: ${
                json.encodeToString(JsonElement.serializer(), Json.parseToJsonElement(body))
            } "
        )
    }
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
            install(LoggingPlugin)
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