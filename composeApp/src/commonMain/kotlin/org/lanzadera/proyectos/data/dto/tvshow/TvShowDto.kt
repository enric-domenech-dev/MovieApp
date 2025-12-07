package org.lanzadera.proyectos.data.dto.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lanzadera.proyectos.data.dto.common.GenreDto
import org.lanzadera.proyectos.data.dto.common.ProductionCompanyDto
import org.lanzadera.proyectos.data.dto.common.ProductionCountryDto
import org.lanzadera.proyectos.data.dto.common.SpokenLanguageDto

@Serializable
data class TvShowDto(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("last_air_date") val lastAirDate: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    @SerialName("origin_country") val originCountry: List<String>? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int? = null,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int? = null,
    val status: String? = null,
    @SerialName("in_production") val inProduction: Boolean? = null,
    val type: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    val genres: List<GenreDto>? = null,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompanyDto>? = null,
    @SerialName("production_countries") val productionCountries: List<ProductionCountryDto>? = null,
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto>? = null,
    val seasons: List<SeasonDto>? = null,
    val tagline: String? = null,
    val homepage: String? = null,
    val budget: Int? = null,
    val revenue: Int? = null,
    val runtime: List<Int>? = null,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>? = null,
    @SerialName("aggregate_credits") val aggregateCredits: AggregateCreditsDto? = null,
    @SerialName("created_by") val createdBy: List<CreatedByDto>? = null,
    val networks: List<NetworkDto>? = null,
    @SerialName("last_episode_to_air") val lastEpisodeToAir: EpisodeDto? = null,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: EpisodeDto? = null,
    val languages: List<String>? = null
)
