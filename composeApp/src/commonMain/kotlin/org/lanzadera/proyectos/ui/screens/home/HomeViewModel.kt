package org.lanzadera.proyectos.ui.screens.home

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.InternalAPI

class HomeViewModel {
    private val client = HttpClient()

    @OptIn(InternalAPI::class)
    suspend fun greeting(): String {
        val response = client.get("https://ktor.io/docs/")
        val body = response. bodyAsText()
        client. close()
        println("--> ${response.request.method.value}  ${response.request.url} ")
        println(body)
        println("<-- END ${response.request.method.value}  ${response.request.url}")
        println("<-- RESPONSE CODE ${response.status}")
        return body
    }
}