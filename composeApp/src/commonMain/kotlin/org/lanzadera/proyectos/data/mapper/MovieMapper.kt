package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.movie.CollectionDto
import org.lanzadera.proyectos.data.dto.movie.MovieDto
import org.lanzadera.proyectos.data.dto.movie.MovieResponseDto
import org.lanzadera.proyectos.domain.models.movie.Collection
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.movie.MovieResponse

fun CollectionDto.toDomain(): Collection = Collection(
    id = id,
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath
)

fun MovieDto.toDomain(): Movie = Movie(
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverageDouble = voteAverage,
    voteCount = voteCount,
    mediaType = mediaType,
    budget = budget,
    revenue = revenue,
    runtime = runtime,
    status = status,
    tagline = tagline,
    imdbId = imdbId,
    homepage = homepage,
    genres = genres?.map { it.toDomain() },
    productionCompanies = productionCompanies?.map { it.toDomain() },
    productionCountries = productionCountries?.map { it.toDomain() },
    spokenLanguages = spokenLanguages?.map { it.toDomain() },
    belongsToCollection = belongsToCollection?.toDomain(),
    aggregateCredits = aggregateCredits?.toDomain()
)

fun MovieResponseDto.toDomain(): MovieResponse = MovieResponse(
    page = page,
    results = results.map { it.toDomain() },
    totalPages = totalPages,
    totalResults = totalResults
)

fun List<MovieDto>.toDomainMovies(): List<Movie> = map { it.toDomain() }
