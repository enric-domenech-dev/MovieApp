package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.tvshow.CreatedByDto
import org.lanzadera.proyectos.data.dto.tvshow.EpisodeDto
import org.lanzadera.proyectos.data.dto.tvshow.NetworkDto
import org.lanzadera.proyectos.data.dto.tvshow.SeasonDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowResponseDto
import org.lanzadera.proyectos.domain.models.tvshow.CreatedBy
import org.lanzadera.proyectos.domain.models.tvshow.Episode
import org.lanzadera.proyectos.domain.models.tvshow.Network
import org.lanzadera.proyectos.domain.models.tvshow.Season
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.models.tvshow.TvShowResponse

fun TvShowDto.toDomain(): TvShow = TvShow(
    id = id,
    name = name,
    originalName = originalName,
    firstAirDate = firstAirDate,
    lastAirDate = lastAirDate,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    backdropPath = backdropPath,
    genreIds = genreIds,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    voteAverageDouble = voteAverage,
    voteCount = voteCount,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    status = status,
    inProduction = inProduction,
    type = type,
    mediaType = mediaType,
    genres = genres?.map { it.toDomain() },
    productionCompanies = productionCompanies?.map { it.toDomain() },
    productionCountries = productionCountries?.map { it.toDomain() },
    spokenLanguages = spokenLanguages?.map { it.toDomain() },
    seasons = seasons?.map { it.toDomain() },
    tagline = tagline,
    homepage = homepage,
    budget = budget,
    revenue = revenue,
    runtime = runtime,
    episodeRunTime = episodeRunTime,
    aggregateCredits = aggregateCredits?.toDomain(),
    createdBy = createdBy?.map { it.toDomain() },
    networks = networks?.map { it.toDomain() },
    lastEpisodeToAir = lastEpisodeToAir?.toDomain(),
    nextEpisodeToAir = nextEpisodeToAir?.toDomain(),
    languages = languages
)

fun TvShowResponseDto.toDomain(): TvShowResponse = TvShowResponse(
    page = page,
    results = results.map { it.toDomain() },
    totalPages = totalPages,
    totalResults = totalResults
)

fun SeasonDto.toDomain(): Season = Season(
    id = id,
    name = name,
    seasonNumber = seasonNumber,
    episodeCount = episodeCount,
    airDate = airDate,
    overview = overview,
    posterPath = posterPath,
    episodes = episodes?.map { it.toDomain() }
)

fun EpisodeDto.toDomain(): Episode = Episode(
    id = id,
    name = name,
    episodeNumber = episodeNumber,
    seasonNumber = seasonNumber,
    airDate = airDate,
    overview = overview,
    runtime = runtime,
    stillPath = stillPath,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun NetworkDto.toDomain(): Network = Network(
    id = id,
    logoPath = logoPath,
    name = name,
    originCountry = originCountry
)

fun CreatedByDto.toDomain(): CreatedBy = CreatedBy(
    id = id,
    creditId = creditId,
    name = name,
    gender = gender,
    profilePath = profilePath
)

// Reverse mappings (Domain -> DTO) for serialization

fun Season.toDto(): SeasonDto = SeasonDto(
    id = id,
    name = name,
    seasonNumber = seasonNumber,
    episodeCount = episodeCount,
    airDate = airDate,
    overview = overview,
    posterPath = posterPath,
    episodes = episodes?.map { it.toDto() }
)

fun Episode.toDto(): EpisodeDto = EpisodeDto(
    id = id,
    name = name,
    episodeNumber = episodeNumber,
    seasonNumber = seasonNumber,
    airDate = airDate,
    overview = overview,
    runtime = runtime,
    stillPath = stillPath,
    voteAverage = voteAverage,
    voteCount = voteCount
)

