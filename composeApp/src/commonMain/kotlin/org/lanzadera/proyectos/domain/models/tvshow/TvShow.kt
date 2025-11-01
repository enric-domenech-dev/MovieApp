package org.lanzadera.proyectos.domain.models.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShow(
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
    @SerialName("vote_average") private val voteAverageDouble: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int? = null,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int? = null,
    val status: String? = null,
    @SerialName("in_production") val inProduction: Boolean? = null,
    val type: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    val genres: List<Genre>? = null,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompany>? = null,
    @SerialName("production_countries") val productionCountries: List<ProductionCountry>? = null,
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguage>? = null,
    val seasons: List<Season>? = null,
    val tagline: String? = null,
    val homepage: String? = null,
    val budget: Int? = null,
    val revenue: Int? = null,
    val runtime: List<Int>? = null,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>? = null,
    @SerialName("aggregate_credits") val aggregateCredits: AggregateCredits? = null,
    @SerialName("created_by") val createdBy: List<CreatedBy>? = null,
    val networks: List<Network>? = null,
    @SerialName("last_episode_to_air") val lastEpisodeToAir: Episode? = null,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: Episode? = null,
    val languages: List<String>? = null
) {
    val voteAverage: String
        get() = voteAverageDouble?.let { (it * 10).toInt() / 10.0 }?.toString() ?: "N/A"
}

@Serializable
data class Genre(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class ProductionCompany(
    val id: Int? = null,
    @SerialName("logo_path") val logoPath: String? = null,
    val name: String? = null,
    @SerialName("origin_country") val originCountry: String? = null
)

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1") val isoCode: String? = null,
    val name: String? = null
)

@Serializable
data class SpokenLanguage(
    @SerialName("english_name") val englishName: String? = null,
    @SerialName("iso_639_1") val isoCode: String? = null,
    val name: String? = null
)

@Serializable
data class Season(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("season_number") val seasonNumber: Int? = null,
    @SerialName("episode_count") val episodeCount: Int? = null,
    @SerialName("air_date") val airDate: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    val episodes: List<Episode>? = null
)

@Serializable
data class Episode(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("episode_number") val episodeNumber: Int? = null,
    @SerialName("season_number") val seasonNumber: Int? = null,
    @SerialName("air_date") val airDate: String? = null,
    val overview: String? = null,
    val runtime: Int? = null,
    @SerialName("still_path") val stillPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null
)

@Serializable
data class AggregateCast(
    val id: Int? = null,
    val adult: Boolean? = null,
    val gender: Int? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    val popularity: Double? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    val roles: List<CastRole>? = null,
    val order: Int? = null,
    @SerialName("character") val characterName: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null
)

@Serializable
data class CastRole(
    @SerialName("credit_id") val creditId: String? = null,
    val character: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null
)

@Serializable
data class AggregateCrew(
    val id: Int? = null,
    val adult: Boolean? = null,
    val gender: Int? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    val popularity: Double? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    val jobs: List<CrewJob>? = null,
    val department: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null
)

@Serializable
data class CrewJob(
    @SerialName("credit_id") val creditId: String? = null,
    val job: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null
)

@Serializable
data class AggregateCredits(
    val cast: List<AggregateCast>? = null,
    val crew: List<AggregateCrew>? = null
)

@Serializable
data class CreatedBy(
    val id: Int? = null,
    @SerialName("credit_id") val creditId: String? = null,
    val name: String? = null,
    val gender: Int? = null,
    @SerialName("profile_path") val profilePath: String? = null
)

@Serializable
data class Network(
    val id: Int? = null,
    @SerialName("logo_path") val logoPath: String? = null,
    val name: String? = null,
    @SerialName("origin_country") val originCountry: String? = null
)
