package org.lanzadera.proyectos.models.user

import androidx.compose.ui.text.intl.Locale
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val id: String = "",
    val password: String,
    val register_date: String = "",
    val user_name: String = "Guest",
    val user_photo: String = ""
) {
    init {
        require(email.contains("@")) { "El email debe ser válido" }
        require(password.length >= 3) { "La contraseña debe tener al menos 3 caracteres" }
    }
}

