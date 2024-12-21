package org.lanzadera.proyectos.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.models.user.User

class LoginViewModel : ViewModel() {

    private val client = HttpClient()

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoginSuccessful = MutableStateFlow<Boolean?>(null) // null = no se ha intentado
    val isLoginSuccessful: StateFlow<Boolean?> = _isLoginSuccessful

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = client.get("https://6764320b52b2a7619f5bc6d6.mockapi.io/garmindata")
                val body = response.bodyAsText()
                client.close()

                println("--> ${response.request.method.value}  ${response.request.url} ")
                println(body)
                println("<-- END ${response.request.method.value}  ${response.request.url}")
                println("<-- RESPONSE CODE ${response.status}")

                val users: List<User> = Json.decodeFromString(body)

                val user = users.find { it.email == email && it.password == password }
                if (user != null) {
                    _userState.value = user
                    _isLoginSuccessful.value = true
                    println("--> Login exitoso!")
                    println("Email: $email")
                    println("Password: $password")
                } else {
                    _isLoginSuccessful.value = false
                    println("Login fallido: Usuario no encontrado o credenciales inválidas.")
                }
            } catch (e: Exception) {
                _isLoginSuccessful.value = false // Maneja errores de red o de lógica
            } finally {
                _isLoading.value = false
            }
        }
    }
}