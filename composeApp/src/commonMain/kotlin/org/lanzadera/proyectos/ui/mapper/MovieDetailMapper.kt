package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.movie.*
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCredits
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCast
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCrew
import org.lanzadera.proyectos.ui.models.*

/**
 * Maps domain Movie (with full details) to UI MovieDetailUI.
 */
fun Movie.toDetailUI(): MovieDetailUI = MovieDetailUI(
    id = id ?: 0,
    title = title ?: "",
    originalTitle = originalTitle,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    runtime = runtime,
    status = status,
    tagline = tagline,
    budget = budget,
    revenue = revenue,
    homepage = homepage,
    imdbId = imdbId,
    originalLanguage = originalLanguage,
    adult = adult,
    video = video,
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
    belongsToCollection = belongsToCollection?.let { 
        CollectionUI(
            id = it.id ?: 0, 
            name = it.name ?: "", 
            posterPath = it.posterPath, 
            backdropPath = it.backdropPath
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
    }
)
