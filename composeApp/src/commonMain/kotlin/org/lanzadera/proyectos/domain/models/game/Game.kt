package org.lanzadera.proyectos.domain.models.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: Int? = null,
    val name: String? = null,
    val summary: String? = null,
    val storyline: String? = null,
    val rating: Double? = null,
    @SerialName("rating_count") val ratingCount: Int? = null,
    val genres: List<Genre>? = null,
    val platforms: List<Platform>? = null,
    @SerialName("release_dates") val releaseDates: List<ReleaseDate>? = null,
    @SerialName("cover") val cover: Cover? = null,
    @SerialName("screenshots") val screenshots: List<Screenshot>? = null,
    val developers: List<Company>? = null,
    val publishers: List<Company>? = null,
    val keywords: List<Keyword>? = null,
    @SerialName("involved_companies") val involvedCompanies: List<InvolvedCompany>? = null,
    @SerialName("artworks") val artworks: List<Artwork>? = null,
    val websites: List<Website>? = null,
    @SerialName("game_engines") val gameEngines: List<GameEngine>? = null,
    @SerialName("game_modes") val gameModes: List<GameMode>? = null,
    val popularity: Double? = null,
    val slug: String? = null,
    val url: String? = null,
    val status: Int? = null,
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    @SerialName("hype") val hype: Int? = null,
    @SerialName("updated_at") val updatedAt: Long? = null,
    @SerialName("created_at") val createdAt: Long? = null,
) {
    val ratingFormatted: String
        get() = rating?.let { (kotlin.math.round(it * 10) / 10.0).toString() } ?: "N/A"

    override fun toString(): String {
        return buildString {
            append("Detalles del juego:\n")
            append("Título: ${name ?: "N/A"}\n")
            append("ID: ${id ?: "N/A"}\n")
            append("Rating: ${rating ?: "N/A"}\n")
            append("Resumen: ${summary ?: "N/A"}\n")
            if (!genres.isNullOrEmpty()) {
                append("Géneros: ${genres.joinToString(", ") { it.name ?: "Desconocido" }}\n")
            }
            if (!platforms.isNullOrEmpty()) {
                append("Plataformas: ${platforms.joinToString(", ") { it.name ?: "Desconocida" }}\n")
            }
        }
    }
}

@Serializable
data class Genre(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null
)

@Serializable
data class Platform(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    @SerialName("abbreviation") val abbreviation: String? = null,
    val url: String? = null
)

@Serializable
data class ReleaseDate(
    val id: Int? = null,
    val category: Int? = null,
    val platform: Int? = null,
    val date: Long? = null,
    val year: Int? = null,
    val region: Int? = null
)

@Serializable
data class Cover(
    val id: Int? = null,
    val url: String? = null,
    @SerialName("image_id") val imageId: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("alpha_channel") val alphaChannel: Boolean? = null,
    val animated: Boolean? = null
) {
    fun getImageUrl(): String {
        return if (!imageId.isNullOrEmpty()) {
            "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.jpg"
        } else {
            url ?: ""
        }
    }
}

@Serializable
data class Screenshot(
    val id: Int? = null,
    val url: String? = null,
    @SerialName("image_id") val imageId: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("alpha_channel") val alphaChannel: Boolean? = null,
    val animated: Boolean? = null
) {
    fun getImageUrl(): String {
        return if (!imageId.isNullOrEmpty()) {
            "https://images.igdb.com/igdb/image/upload/t_screenshot_big/$imageId.jpg"
        } else {
            url ?: ""
        }
    }
}

@Serializable
data class Company(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null,
    val logo: Cover? = null
)

@Serializable
data class Keyword(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null
)

@Serializable
data class InvolvedCompany(
    val id: Int? = null,
    val company: Company? = null,
    val developer: Boolean? = null,
    val publisher: Boolean? = null,
    val porting: Boolean? = null,
    val supporting: Boolean? = null
)

@Serializable
data class Artwork(
    val id: Int? = null,
    val url: String? = null,
    @SerialName("image_id") val imageId: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("alpha_channel") val alphaChannel: Boolean? = null,
    val animated: Boolean? = null
) {
    fun getImageUrl(): String {
        return if (!imageId.isNullOrEmpty()) {
            "https://images.igdb.com/igdb/image/upload/t_artwork_big/$imageId.jpg"
        } else {
            url ?: ""
        }
    }
}

@Serializable
data class Website(
    val id: Int? = null,
    val category: Int? = null,
    val url: String? = null,
    @SerialName("trusted") val trusted: Boolean? = null
)

@Serializable
data class GameEngine(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null,
    val logo: Cover? = null
)

@Serializable
data class GameMode(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null
)

