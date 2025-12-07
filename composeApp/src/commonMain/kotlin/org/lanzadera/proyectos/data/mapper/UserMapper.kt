package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.user.UserDto
import org.lanzadera.proyectos.domain.models.user.User

// UserDto → User (Domain)
fun UserDto.toDomain(): User = User(
    email = email,
    id = id,
    password = password,
    registerDate = registerDate,
    userName = userName,
    userPhoto = userPhoto
)

// User (Domain) → UserDto
fun User.toDto(): UserDto = UserDto(
    email = email,
    id = id,
    password = password,
    registerDate = registerDate,
    userName = userName,
    userPhoto = userPhoto
)
