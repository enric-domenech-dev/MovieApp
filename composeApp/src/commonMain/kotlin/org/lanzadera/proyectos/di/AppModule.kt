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
import org.lanzadera.proyectos.data.repository.LoadInitialDataImpl
import org.lanzadera.proyectos.domain.repository.LoadInitialData
import org.lanzadera.proyectos.domain.usecase.load_initial_data.LoadInitialDataUseCase
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

    // HTTPS Client
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
}

val viewModelsModule = module {

    // UseCases
    single { LoadInitialDataUseCase(get()) }

    // Repositories
    single<LoadInitialData> { LoadInitialDataImpl(get(), 5, get()) }

    // ViewModels
    viewModel { HomeViewModel(get()) }
}

val nativeModule: Module = module {}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}