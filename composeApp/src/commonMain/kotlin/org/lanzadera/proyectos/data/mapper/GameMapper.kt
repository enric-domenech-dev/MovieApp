package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.game.*
import org.lanzadera.proyectos.domain.models.game.*

// DTO → Domain
fun GameDto.toDomain() = Game(
    id = id,
    name = name,
    summary = summary,
    storyline = storyline,
    rating = rating,
    ratingCount = ratingCount,
    genres = genres?.map { it.toDomain() },
    platforms = platforms?.map { it.toDomain() },
    releaseDates = releaseDates?.map { it.toDomain() },
    cover = cover?.toDomain(),
    screenshots = screenshots?.map { it.toDomain() },
    developers = developers?.map { it.toDomain() },
    publishers = publishers?.map { it.toDomain() },
    keywords = keywords?.map { it.toDomain() },
    involvedCompanies = involvedCompanies?.map { it.toDomain() },
    artworks = artworks?.map { it.toDomain() },
    websites = websites?.map { it.toDomain() },
    gameEngines = gameEngines?.map { it.toDomain() },
    gameModes = gameModes?.map { it.toDomain() },
    popularity = popularity,
    slug = slug,
    url = url,
    status = status,
    firstReleaseDate = firstReleaseDate,
    hype = hype,
    updatedAt = updatedAt,
    createdAt = createdAt
)

fun GenreDto.toDomain() = Genre(
    id = id,
    name = name,
    slug = slug,
    url = url
)

fun PlatformDto.toDomain() = Platform(
    id = id,
    name = name,
    slug = slug,
    abbreviation = abbreviation,
    url = url
)

fun ReleaseDateDto.toDomain() = ReleaseDate(
    id = id,
    category = category,
    platform = platform,
    date = date,
    year = year,
    region = region
)

fun CoverDto.toDomain() = Cover(
    id = id,
    url = url,
    imageId = imageId,
    height = height,
    width = width,
    alphaChannel = alphaChannel,
    animated = animated
)

fun ScreenshotDto.toDomain() = Screenshot(
    id = id,
    url = url,
    imageId = imageId,
    height = height,
    width = width,
    alphaChannel = alphaChannel,
    animated = animated
)

fun CompanyDto.toDomain() = Company(
    id = id,
    name = name,
    slug = slug,
    url = url,
    logo = logo?.toDomain()
)

fun KeywordDto.toDomain() = Keyword(
    id = id,
    name = name,
    slug = slug,
    url = url
)

fun InvolvedCompanyDto.toDomain() = InvolvedCompany(
    id = id,
    company = company?.toDomain(),
    developer = developer,
    publisher = publisher,
    porting = porting,
    supporting = supporting
)

fun ArtworkDto.toDomain() = Artwork(
    id = id,
    url = url,
    imageId = imageId,
    height = height,
    width = width,
    alphaChannel = alphaChannel,
    animated = animated
)

fun WebsiteDto.toDomain() = Website(
    id = id,
    category = category,
    url = url,
    trusted = trusted
)

fun GameEngineDto.toDomain() = GameEngine(
    id = id,
    name = name,
    slug = slug,
    url = url,
    logo = logo?.toDomain()
)

fun GameModeDto.toDomain() = GameMode(
    id = id,
    name = name,
    slug = slug,
    url = url
)

// Domain → DTO (for reverse mapping, e.g., Room serialization)
fun Game.toDto() = GameDto(
    id = id,
    name = name,
    summary = summary,
    storyline = storyline,
    rating = rating,
    ratingCount = ratingCount,
    genres = genres?.map { it.toDto() },
    platforms = platforms?.map { it.toDto() },
    releaseDates = releaseDates?.map { it.toDto() },
    cover = cover?.toDto(),
    screenshots = screenshots?.map { it.toDto() },
    developers = developers?.map { it.toDto() },
    publishers = publishers?.map { it.toDto() },
    keywords = keywords?.map { it.toDto() },
    involvedCompanies = involvedCompanies?.map { it.toDto() },
    artworks = artworks?.map { it.toDto() },
    websites = websites?.map { it.toDto() },
    gameEngines = gameEngines?.map { it.toDto() },
    gameModes = gameModes?.map { it.toDto() },
    popularity = popularity,
    slug = slug,
    url = url,
    status = status,
    firstReleaseDate = firstReleaseDate,
    hype = hype,
    updatedAt = updatedAt,
    createdAt = createdAt
)

fun Genre.toDto() = GenreDto(
    id = id,
    name = name,
    slug = slug,
    url = url
)

fun Platform.toDto() = PlatformDto(
    id = id,
    name = name,
    slug = slug,
    abbreviation = abbreviation,
    url = url
)

fun ReleaseDate.toDto() = ReleaseDateDto(
    id = id,
    category = category,
    platform = platform,
    date = date,
    year = year,
    region = region
)

fun Cover.toDto() = CoverDto(
    id = id,
    url = url,
    imageId = imageId,
    height = height,
    width = width,
    alphaChannel = alphaChannel,
    animated = animated
)

fun Screenshot.toDto() = ScreenshotDto(
    id = id,
    url = url,
    imageId = imageId,
    height = height,
    width = width,
    alphaChannel = alphaChannel,
    animated = animated
)

fun Company.toDto() = CompanyDto(
    id = id,
    name = name,
    slug = slug,
    url = url,
    logo = logo?.toDto()
)

fun Keyword.toDto() = KeywordDto(
    id = id,
    name = name,
    slug = slug,
    url = url
)

fun InvolvedCompany.toDto() = InvolvedCompanyDto(
    id = id,
    company = company?.toDto(),
    developer = developer,
    publisher = publisher,
    porting = porting,
    supporting = supporting
)

fun Artwork.toDto() = ArtworkDto(
    id = id,
    url = url,
    imageId = imageId,
    height = height,
    width = width,
    alphaChannel = alphaChannel,
    animated = animated
)

fun Website.toDto() = WebsiteDto(
    id = id,
    category = category,
    url = url,
    trusted = trusted
)

fun GameEngine.toDto() = GameEngineDto(
    id = id,
    name = name,
    slug = slug,
    url = url,
    logo = logo?.toDto()
)

fun GameMode.toDto() = GameModeDto(
    id = id,
    name = name,
    slug = slug,
    url = url
)
