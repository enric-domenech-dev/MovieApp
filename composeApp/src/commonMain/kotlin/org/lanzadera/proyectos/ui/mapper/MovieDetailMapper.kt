package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.movie.*
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
    voteAverage = voteAverage?.let { "%.1f".format(it) } ?: "0.0",
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
        ProductionCountryUI(iso = it.iso31661 ?: "", name = it.name ?: "") 
    },
    spokenLanguages = spokenLanguages?.map { 
        SpokenLanguageUI(iso = it.iso6391 ?: "", name = it.name ?: "", englishName = it.englishName) 
    },
    credits = credits?.let { 
        CreditsUI(
            cast = it.cast?.map { c -> 
                CastUI(
                    id = c.id ?: 0, 
                    name = c.name ?: "", 
                    character = c.character, 
                    profilePath = c.profilePath, 
                    order = c.order
                ) 
            },
            crew = it.crew?.map { c -> 
                CrewUI(
                    id = c.id ?: 0, 
                    name = c.name ?: "", 
                    job = c.job, 
                    department = c.department, 
                    profilePath = c.profilePath
                ) 
            }
        ) 
    },
    videos = videos?.let { 
        VideosUI(
            results = it.results?.map { v -> 
                VideoUI(
                    id = v.id ?: "", 
                    key = v.key ?: "", 
                    name = v.name ?: "", 
                    site = v.site ?: "", 
                    type = v.type ?: "", 
                    official = v.official
                ) 
            }
        ) 
    },
    images = images?.let { 
        ImagesUI(
            backdrops = it.backdrops?.map { img -> 
                ImageUI(filePath = img.filePath ?: "", width = img.width, height = img.height) 
            },
            posters = it.posters?.map { img -> 
                ImageUI(filePath = img.filePath ?: "", width = img.width, height = img.height) 
            }
        ) 
    },
    keywords = keywords?.let { 
        KeywordsUI(
            keywords = it.keywords?.map { k -> KeywordUI(id = k.id ?: 0, name = k.name ?: "") }
        ) 
    },
    recommendations = recommendations?.map { it.toUI() },
    similar = similar?.map { it.toUI() },
    belongsToCollection = belongsToCollection?.let { 
        CollectionUI(
            id = it.id ?: 0, 
            name = it.name ?: "", 
            posterPath = it.posterPath, 
            backdropPath = it.backdropPath
        ) 
    }
)
