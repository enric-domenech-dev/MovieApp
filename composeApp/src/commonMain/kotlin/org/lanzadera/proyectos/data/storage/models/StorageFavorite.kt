package org.lanzadera.proyectos.data.storage.models

data class StorageFavorite(
    val id: String,
    val type: String,
    val title: String,
    val posterUrl: String? = null,
    val overview: String? = null,
    val addedAt: Long,
    val updatedAt: Long
)

