package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.tvshow.*
import org.lanzadera.proyectos.ui.models.*

/**
 * Maps domain TvShow (with full details) to UI TvShowDetailUI.
 */
fun TvShow.toDetailUI(): TvShowDetailUI = TvShowDetailUI(
    id = id ?: 0,
    name = name ?: "",
    originalName = originalName,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    firstAirDate = firstAirDate,
    lastAirDate = lastAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    status = status,
    inProduction = inProduction,
    tagline = tagline,
    homepage = homepage,
    type = type,
    originalLanguage = originalLanguage,
    originCountry = originCountry,
    episodeRunTime = episodeRunTime,
    languages = languages,
    genres = genres?.map { GenreUI(id = it.id ?: 0, name = it.name ?: "") },
    productionCompanies = productionCompanies?.map { 
        ProductionCompanyUI(
            id = it.id ?: 0, 
            name = it.name ?: "", 
            logoPath = it.logoPath, 
            originCountry = it.originCountry
        ) 
    },
    productionCountries = productionCountries?.map { 
        ProductionCountryUI(iso = it.isoCode ?: "", name = it.name ?: "") 
    },
    spokenLanguages = spokenLanguages?.map { 
        SpokenLanguageUI(iso = it.isoCode ?: "", name = it.name ?: "", englishName = it.englishName) 
    },
    seasons = seasons?.map { season ->
        SeasonUI(
            id = season.id ?: 0,
            name = season.name ?: "",
            seasonNumber = season.seasonNumber,
            episodeCount = season.episodeCount,
            posterPath = season.posterPath,
            airDate = season.airDate,
            overview = season.overview,
            episodes = season.episodes?.map { episode ->
                EpisodeUI(
                    id = episode.id ?: 0,
                    name = episode.name ?: "",
                    episodeNumber = episode.episodeNumber,
                    seasonNumber = episode.seasonNumber,
                    airDate = episode.airDate,
                    overview = episode.overview,
                    stillPath = episode.stillPath,
                    voteAverage = episode.voteAverage,
                    voteCount = episode.voteCount
                )
            }
        )
    },
    networks = networks?.map { network ->
        NetworkUI(
            id = network.id ?: 0,
            name = network.name ?: "",
            logoPath = network.logoPath,
            originCountry = network.originCountry
        )
    },
    createdBy = createdBy?.map { creator ->
        CreatedByUI(
            id = creator.id ?: 0,
            name = creator.name ?: "",
            profilePath = creator.profilePath,
            gender = creator.gender
        )
    },
    aggregateCredits = aggregateCredits?.let {
        AggregateCreditsUI(
            cast = it.cast?.map { cast ->
                AggregateCastUI(
                    id = cast.id ?: 0,
                    name = cast.name ?: "",
                    profilePath = cast.profilePath,
                    roles = cast.roles?.map { role ->
                        CastRoleUI(
                            character = role.character ?: "",
                            episodeCount = role.episodeCount
                        )
                    },
                    order = cast.order
                )
            },
            crew = it.crew?.map { crew ->
                AggregateCrewUI(
                    id = crew.id ?: 0,
                    name = crew.name ?: "",
                    profilePath = crew.profilePath,
                    jobs = crew.jobs?.map { job ->
                        CrewJobUI(
                            job = job.job ?: "",
                            episodeCount = job.episodeCount
                        )
                    }
                )
            }
        )
    },
    lastEpisodeToAir = lastEpisodeToAir?.let { episode ->
        EpisodeUI(
            id = episode.id ?: 0,
            name = episode.name ?: "",
            episodeNumber = episode.episodeNumber,
            seasonNumber = episode.seasonNumber,
            airDate = episode.airDate,
            overview = episode.overview,
            stillPath = episode.stillPath,
            voteAverage = episode.voteAverage,
            voteCount = episode.voteCount
        )
    },
    nextEpisodeToAir = nextEpisodeToAir?.let { episode ->
        EpisodeUI(
            id = episode.id ?: 0,
            name = episode.name ?: "",
            episodeNumber = episode.episodeNumber,
            seasonNumber = episode.seasonNumber,
            airDate = episode.airDate,
            overview = episode.overview,
            stillPath = episode.stillPath,
            voteAverage = episode.voteAverage,
            voteCount = episode.voteCount
        )
    }
)
