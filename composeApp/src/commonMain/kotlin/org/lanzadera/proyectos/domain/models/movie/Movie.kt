package org.lanzadera.proyectos.domain.models.movie

import org.lanzadera.proyectos.domain.models.tvshow.AggregateCredits
import org.lanzadera.proyectos.domain.models.tvshow.Genre
import org.lanzadera.proyectos.domain.models.tvshow.ProductionCompany
import org.lanzadera.proyectos.domain.models.tvshow.ProductionCountry
import org.lanzadera.proyectos.domain.models.tvshow.SpokenLanguage

data class Movie(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val genreIds: List<Int>? = null,
    val id: Int? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverageDouble: Double? = null,
    val voteCount: Int? = null,
    val mediaType: String? = null,
    // Detail endpoint additional fields
    val budget: Int? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    val imdbId: String? = null,
    val homepage: String? = null,
    val genres: List<Genre>? = null,
    val productionCompanies: List<ProductionCompany>? = null,
    val productionCountries: List<ProductionCountry>? = null,
    val spokenLanguages: List<SpokenLanguage>? = null,
    val belongsToCollection: Collection? = null,
    val aggregateCredits: AggregateCredits? = null
) {
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

data class Collection(
    val id: Int? = null,
    val name: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null
)


