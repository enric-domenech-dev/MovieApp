package org.lanzadera.proyectos.data.dto.tvshow

import kotlinx.serialization.Serializable

@Serializable
data class AggregateCreditsDto(
    val cast: List<AggregateCastDto>? = null,
    val crew: List<AggregateCrewDto>? = null
)
