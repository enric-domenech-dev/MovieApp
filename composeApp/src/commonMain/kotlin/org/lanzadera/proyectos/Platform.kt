package org.lanzadera.proyectos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform