package org.lanzadera.proyectos.data.dto.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int? = null,
    val name: String? = null,
    val summary: String? = null,
    val storyline: String? = null,
    val rating: Double? = null,
    @SerialName("rating_count") val ratingCount: Int? = null,
    val genres: List<GenreDto>? = null,
    val platforms: List<PlatformDto>? = null,
    @SerialName("release_dates") val releaseDates: List<ReleaseDateDto>? = null,
    @SerialName("cover") val cover: CoverDto? = null,
    @SerialName("screenshots") val screenshots: List<ScreenshotDto>? = null,
    val developers: List<CompanyDto>? = null,
    val publishers: List<CompanyDto>? = null,
    val keywords: List<KeywordDto>? = null,
    @SerialName("involved_companies") val involvedCompanies: List<InvolvedCompanyDto>? = null,
    @SerialName("artworks") val artworks: List<ArtworkDto>? = null,
    val websites: List<WebsiteDto>? = null,
    @SerialName("game_engines") val gameEngines: List<GameEngineDto>? = null,
    @SerialName("game_modes") val gameModes: List<GameModeDto>? = null,
    val popularity: Double? = null,
    val slug: String? = null,
    val url: String? = null,
    val status: Int? = null,
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    @SerialName("hype") val hype: Int? = null,
    @SerialName("updated_at") val updatedAt: Long? = null,
    @SerialName("created_at") val createdAt: Long? = null
)

@Serializable
data class GenreDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null
)

@Serializable
data class PlatformDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    @SerialName("abbreviation") val abbreviation: String? = null,
    val url: String? = null
)

@Serializable
data class ReleaseDateDto(
    val id: Int? = null,
    val category: Int? = null,
    val platform: Int? = null,
    val date: Long? = null,
    val year: Int? = null,
    val region: Int? = null
)

@Serializable
data class CoverDto(
    val id: Int? = null,
    val url: String? = null,
    @SerialName("image_id") val imageId: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("alpha_channel") val alphaChannel: Boolean? = null,
    val animated: Boolean? = null
)

@Serializable
data class ScreenshotDto(
    val id: Int? = null,
    val url: String? = null,
    @SerialName("image_id") val imageId: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("alpha_channel") val alphaChannel: Boolean? = null,
    val animated: Boolean? = null
)

@Serializable
data class CompanyDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null,
    val logo: CoverDto? = null
)

@Serializable
data class KeywordDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null
)

@Serializable
data class InvolvedCompanyDto(
    val id: Int? = null,
    val company: CompanyDto? = null,
    val developer: Boolean? = null,
    val publisher: Boolean? = null,
    val porting: Boolean? = null,
    val supporting: Boolean? = null
)

@Serializable
data class ArtworkDto(
    val id: Int? = null,
    val url: String? = null,
    @SerialName("image_id") val imageId: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("alpha_channel") val alphaChannel: Boolean? = null,
    val animated: Boolean? = null
)

@Serializable
data class WebsiteDto(
    val id: Int? = null,
    val category: Int? = null,
    val url: String? = null,
    @SerialName("trusted") val trusted: Boolean? = null
)

@Serializable
data class GameEngineDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null,
    val logo: CoverDto? = null
)

@Serializable
data class GameModeDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val url: String? = null
)
