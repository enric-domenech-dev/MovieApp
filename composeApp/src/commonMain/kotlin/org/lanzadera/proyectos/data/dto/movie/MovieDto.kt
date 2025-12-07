package org.lanzadera.proyectos.data.dto.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lanzadera.proyectos.data.dto.common.GenreDto
import org.lanzadera.proyectos.data.dto.common.ProductionCompanyDto
import org.lanzadera.proyectos.data.dto.common.ProductionCountryDto
import org.lanzadera.proyectos.data.dto.common.SpokenLanguageDto
import org.lanzadera.proyectos.data.dto.tvshow.AggregateCreditsDto

@Serializable
data class MovieDto(
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
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("media_type") val mediaType: String? = null,
    // Detail endpoint additional fields
    val budget: Int? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    val homepage: String? = null,
    val genres: List<GenreDto>? = null,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompanyDto>? = null,
    @SerialName("production_countries") val productionCountries: List<ProductionCountryDto>? = null,
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto>? = null,
    @SerialName("belongs_to_collection") val belongsToCollection: CollectionDto? = null,
    @SerialName("aggregate_credits") val aggregateCredits: AggregateCreditsDto? = null
)
