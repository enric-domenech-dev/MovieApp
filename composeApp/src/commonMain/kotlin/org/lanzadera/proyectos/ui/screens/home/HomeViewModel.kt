package org.lanzadera.proyectos.ui.screens.home

import io.ktor.client.request.*
import io.ktor.client.statement.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import movieapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource


class HomeViewModel : ViewModel() {

    private val client = HttpClient()
    suspend fun data(apiKey: String): String {
        println(apiKey)
        val response: HttpResponse = client.get("https://api.themoviedb.org/3/discover/movie?language=en-US&page=1&sort_by=popularity.desc") {
            headers {
                append("accept", "application/json")
                append(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDBhMzJhZjMxN2Y0MmU2Y2Y3NGMwNDJlYTE0YTJhOCIsIm5iZiI6MTczNDc4NDgxMi40MjUsInN1YiI6IjY3NjZiNzJjMGIyZmJiOWRlYTVlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wGnX7P8oJrWpXdpxd3wQXw2hjw5API7MU3ucBwAKIWU"
                )
            }
        }

        val responseBody: String = response.body()
        val body = response.bodyAsText()
        println(responseBody)
        println(body)
        return body
    }
}