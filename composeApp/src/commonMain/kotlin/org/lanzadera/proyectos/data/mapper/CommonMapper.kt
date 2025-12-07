package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.common.GenreDto
import org.lanzadera.proyectos.data.dto.common.ProductionCompanyDto
import org.lanzadera.proyectos.data.dto.common.ProductionCountryDto
import org.lanzadera.proyectos.data.dto.common.SpokenLanguageDto
import org.lanzadera.proyectos.domain.models.tvshow.Genre
import org.lanzadera.proyectos.domain.models.tvshow.ProductionCompany
import org.lanzadera.proyectos.domain.models.tvshow.ProductionCountry
import org.lanzadera.proyectos.domain.models.tvshow.SpokenLanguage

fun GenreDto.toDomain(): Genre = Genre(
    id = id,
    name = name
)

fun ProductionCompanyDto.toDomain(): ProductionCompany = ProductionCompany(
    id = id,
    logoPath = logoPath,
    name = name,
    originCountry = originCountry
)

fun ProductionCountryDto.toDomain(): ProductionCountry = ProductionCountry(
    isoCode = isoCode,
    name = name
)

fun SpokenLanguageDto.toDomain(): SpokenLanguage = SpokenLanguage(
    englishName = englishName,
    isoCode = isoCode,
    name = name
)

fun List<GenreDto>.toDomainGenres(): List<Genre> = map { it.toDomain() }

fun List<ProductionCompanyDto>.toDomainCompanies(): List<ProductionCompany> = map { it.toDomain() }

fun List<ProductionCountryDto>.toDomainCountries(): List<ProductionCountry> = map { it.toDomain() }

fun List<SpokenLanguageDto>.toDomainLanguages(): List<SpokenLanguage> = map { it.toDomain() }
