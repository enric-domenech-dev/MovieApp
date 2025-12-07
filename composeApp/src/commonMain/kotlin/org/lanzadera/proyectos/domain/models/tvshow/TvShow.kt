package org.lanzadera.proyectos.domain.models.tvshow

data class TvShow(
    val id: Int? = null,
    val name: String? = null,
    val originalName: String? = null,
    val firstAirDate: String? = null,
    val lastAirDate: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val genreIds: List<Int>? = null,
    val originCountry: List<String>? = null,
    val originalLanguage: String? = null,
    val voteAverageDouble: Double? = null,
    val voteCount: Int? = null,
    val numberOfSeasons: Int? = null,
    val numberOfEpisodes: Int? = null,
    val status: String? = null,
    val inProduction: Boolean? = null,
    val type: String? = null,
    val mediaType: String? = null,
    val genres: List<Genre>? = null,
    val productionCompanies: List<ProductionCompany>? = null,
    val productionCountries: List<ProductionCountry>? = null,
    val spokenLanguages: List<SpokenLanguage>? = null,
    val seasons: List<Season>? = null,
    val tagline: String? = null,
    val homepage: String? = null,
    val budget: Int? = null,
    val revenue: Int? = null,
    val runtime: List<Int>? = null,
    val episodeRunTime: List<Int>? = null,
    val aggregateCredits: AggregateCredits? = null,
    val createdBy: List<CreatedBy>? = null,
    val networks: List<Network>? = null,
    val lastEpisodeToAir: Episode? = null,
    val nextEpisodeToAir: Episode? = null,
    val languages: List<String>? = null
) {
    val voteAverage: String
        get() = voteAverageDouble?.let { (it * 10).toInt() / 10.0 }?.toString() ?: "N/A"
}

data class Genre(
    val id: Int? = null,
    val name: String? = null
)

data class ProductionCompany(
    val id: Int? = null,
    val logoPath: String? = null,
    val name: String? = null,
    val originCountry: String? = null
)

data class ProductionCountry(
    val isoCode: String? = null,
    val name: String? = null
)

data class SpokenLanguage(
    val englishName: String? = null,
    val isoCode: String? = null,
    val name: String? = null
)

data class Season(
    val id: Int? = null,
    val name: String? = null,
    val seasonNumber: Int? = null,
    val episodeCount: Int? = null,
    val airDate: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val episodes: List<Episode>? = null
)

data class Episode(
    val id: Int? = null,
    val name: String? = null,
    val episodeNumber: Int? = null,
    val seasonNumber: Int? = null,
    val airDate: String? = null,
    val overview: String? = null,
    val runtime: Int? = null,
    val stillPath: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null
)

data class AggregateCast(
    val id: Int? = null,
    val adult: Boolean? = null,
    val gender: Int? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val originalName: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null,
    val roles: List<CastRole>? = null,
    val order: Int? = null,
    val characterName: String? = null,
    val episodeCount: Int? = null
)

data class CastRole(
    val creditId: String? = null,
    val character: String? = null,
    val episodeCount: Int? = null
)

data class AggregateCrew(
    val id: Int? = null,
    val adult: Boolean? = null,
    val gender: Int? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val originalName: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null,
    val jobs: List<CrewJob>? = null,
    val department: String? = null,
    val episodeCount: Int? = null
)

data class CrewJob(
    val creditId: String? = null,
    val job: String? = null,
    val episodeCount: Int? = null
)

data class AggregateCredits(
    val cast: List<AggregateCast>? = null,
    val crew: List<AggregateCrew>? = null
)

data class CreatedBy(
    val id: Int? = null,
    val creditId: String? = null,
    val name: String? = null,
    val gender: Int? = null,
    val profilePath: String? = null
)

data class Network(
    val id: Int? = null,
    val logoPath: String? = null,
    val name: String? = null,
    val originCountry: String? = null
)
