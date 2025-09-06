package org.lanzadera.proyectos.domain.models.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val adult: Boolean? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    val id: Int? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    @SerialName("vote_average") private val voteAverageDouble: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("media_type") val mediaType: String? = null
) {

    // Propiedad calculada para el voteAverage formateado
    val voteAverage: String
        get() = voteAverageDouble?.let { (it * 10).toInt() / 10.0 }?.toString() ?: "N/A"

    override fun toString(): String {
        return buildString {

            append("Detalles de la película:\n")
            append("Título: ${title ?: "N/A"}\n")
            append("Título original: ${originalTitle ?: "N/A"}\n")
            append("ID: ${id ?: "N/A"}\n")
            append("Puntuación: ${voteCount ?: "N/A"}\n")
            append("Popularidad: ${popularity ?: "N/A"}\n")
            append("Géneros: ${genreIds?.joinToString(", ") ?: "N/A"}\n")
            append("Ruta del cartel: ${posterPath ?: "N/A"}\n")
            append("Ruta del fondo: ${backdropPath ?: "N/A"}\n")
            append("Fecha de estreno: ${releaseDate ?: "N/A"}\n")
            append("Valoración: ${voteAverage}\n")
            append("Adulto: ${if (adult == true) "Sí" else "No"}\n")
            append("Media tipo: ${mediaType ?: "N/A"}\n")
            append("Idioma original: ${originalLanguage ?: "N/A"}\n")
            append("Sinopsis: ${overview ?: "No disponible"}")
        }
    }
}