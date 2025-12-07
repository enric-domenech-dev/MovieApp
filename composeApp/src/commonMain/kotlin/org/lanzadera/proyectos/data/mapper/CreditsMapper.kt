package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.tvshow.AggregateCastDto
import org.lanzadera.proyectos.data.dto.tvshow.AggregateCreditsDto
import org.lanzadera.proyectos.data.dto.tvshow.AggregateCrewDto
import org.lanzadera.proyectos.data.dto.tvshow.CastRoleDto
import org.lanzadera.proyectos.data.dto.tvshow.CrewJobDto
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCast
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCredits
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCrew
import org.lanzadera.proyectos.domain.models.tvshow.CastRole
import org.lanzadera.proyectos.domain.models.tvshow.CrewJob

fun CastRoleDto.toDomain(): CastRole = CastRole(
    creditId = creditId,
    character = character,
    episodeCount = episodeCount
)

fun AggregateCastDto.toDomain(): AggregateCast = AggregateCast(
    id = id,
    adult = adult,
    gender = gender,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath,
    roles = roles?.map { it.toDomain() },
    order = order,
    characterName = characterName,
    episodeCount = episodeCount
)

fun CrewJobDto.toDomain(): CrewJob = CrewJob(
    creditId = creditId,
    job = job,
    episodeCount = episodeCount
)

fun AggregateCrewDto.toDomain(): AggregateCrew = AggregateCrew(
    id = id,
    adult = adult,
    gender = gender,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath,
    jobs = jobs?.map { it.toDomain() },
    department = department,
    episodeCount = episodeCount
)

fun AggregateCreditsDto.toDomain(): AggregateCredits = AggregateCredits(
    cast = cast?.map { it.toDomain() },
    crew = crew?.map { it.toDomain() }
)
