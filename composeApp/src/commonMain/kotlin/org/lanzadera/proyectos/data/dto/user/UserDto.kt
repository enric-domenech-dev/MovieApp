package org.lanzadera.proyectos.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String,
    val id: String = "",
    val password: String,
    @SerialName("register_date") val registerDate: String = "",
    @SerialName("user_name") val userName: String = "Guest",
    @SerialName("user_photo") val userPhoto: String = ""
)
