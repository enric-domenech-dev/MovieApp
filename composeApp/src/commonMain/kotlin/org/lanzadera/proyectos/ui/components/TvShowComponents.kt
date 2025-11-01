package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import kotlinx.datetime.LocalDate
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.film
import org.jetbrains.compose.resources.painterResource
import org.lanzadera.proyectos.domain.models.tvshow.Episode
import org.lanzadera.proyectos.domain.models.tvshow.Season
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.navigation.NavigationStore
import org.lanzadera.proyectos.utils.Constants

@Composable
fun TvShowItem(
    nav: NavHostController,
    tvShow: TvShow,
    modifier: Modifier = Modifier.wrapContentHeight(),
    showMeta: Boolean = true
) {
    Column(
        modifier = modifier
            .clickable {
                NavigationStore.selectedTvShow = tvShow
                nav.navigate(Constants.Screen.SeriesDetail.route)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${tvShow.posterPath}",
                contentDescription = tvShow.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )
        }

        if (showMeta) {
            Spacer(modifier = Modifier.height(6.dp))
            tvShow.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val formattedDate = remember(tvShow.firstAirDate) {
                tvShow.firstAirDate?.let {
                    try {
                        val date = LocalDate.parse(it)
                        "${date.dayOfMonth} ${
                            date.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }
                        } ${date.year}"
                    } catch (e: Exception) {
                        it
                    }
                }
            }
            formattedDate?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
fun TvShowHeader(modifier: Modifier = Modifier, nav: NavHostController, tvShow: TvShow) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedTvShow = tvShow
                nav.navigate(Constants.Screen.SeriesDetail.route)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${tvShow.posterPath}",
                contentDescription = tvShow.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        tvShow.name?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        tvShow.firstAirDate?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun TvShowSubheader(modifier: Modifier = Modifier, nav: NavHostController, tvShow: TvShow, showMeta: Boolean) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedTvShow = tvShow
                nav.navigate(Constants.Screen.SeriesDetail.route)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${tvShow.posterPath}",
                contentDescription = tvShow.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )
        }
        if (showMeta) {
            Spacer(modifier = Modifier.height(6.dp))
            tvShow.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            tvShow.firstAirDate?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
fun TvShowDetail(tvShow: TvShow?) {
    val isFavorite = rememberSaveable { mutableStateOf(false) }
    if (tvShow == null) return

    Column {
        // Backdrop
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${tvShow.backdropPath}",
                contentDescription = tvShow.name,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
        }

        // Header con fecha, rating y favorito
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tvShow.firstAirDate?.let {
                val date = try {
                    LocalDate.parse(it)
                } catch (e: Exception) {
                    null
                }
                date?.let {
                    val formattedDate = "${it.dayOfMonth} ${
                        it.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }
                    } ${it.year}"
                    Text(
                        text = formattedDate,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CircularAvgVotes(tvShow.voteAverage.toDoubleOrNull() ?: 0.0)

            Spacer(modifier = Modifier.width(16.dp))

            FloatingActionButton(
                onClick = {
                    isFavorite.value = !isFavorite.value
                },
                content = {
                    Icon(
                        imageVector = if (isFavorite.value) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite Button"
                    )
                },
                shape = RoundedCornerShape(50),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            item {
                // Título y meta info
                tvShow.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Info línea: status, temporadas, episodios
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    tvShow.status?.let {
                        Text(
                            text = "Status: $it",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    tvShow.numberOfSeasons?.let {
                        Text(
                            text = "$it Seasons",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    tvShow.numberOfEpisodes?.let {
                        Text(
                            text = "$it Episodes",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Géneros
                if (!tvShow.genres.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tvShow.genres.forEach { genre ->
                            Card(
                                modifier = Modifier.wrapContentSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = genre.name ?: "",
                                    modifier = Modifier.padding(8.dp),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Overview
                tvShow.overview?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Creadores
                if (!tvShow.createdBy.isNullOrEmpty()) {
                    Text(
                        text = "Created By",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tvShow.createdBy.map { it.name }.joinToString(", "),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Redes de transmisión
                if (!tvShow.networks.isNullOrEmpty()) {
                    Text(
                        text = "Networks",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tvShow.networks.forEach { network ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.wrapContentSize()
                            ) {
                                if (!network.logoPath.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w200${network.logoPath}",
                                        contentDescription = network.name,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .height(40.dp)
                                            .wrapContentWidth()
                                    )
                                }
                                network.name?.let {
                                    Text(
                                        text = it,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Países de origen
                if (!tvShow.originCountry.isNullOrEmpty()) {
                    Text(
                        text = "Country: ${tvShow.originCountry.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Idiomas
                if (!tvShow.spokenLanguages.isNullOrEmpty()) {
                    Text(
                        text = "Languages: ${
                            tvShow.spokenLanguages.map { it.englishName ?: it.name }.joinToString(", ")
                        }",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Productoras
                if (!tvShow.productionCompanies.isNullOrEmpty()) {
                    Text(
                        text = "Production: ${tvShow.productionCompanies.map { it.name }.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Último episodio emitido
                tvShow.lastEpisodeToAir?.let { episode ->
                    Text(
                        text = "Last Episode Aired",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "S${
                                    episode.seasonNumber?.toString()?.padStart(2, '0')
                                }: E${
                                    episode.episodeNumber?.toString()?.padStart(2, '0')
                                } - ${episode.name ?: "Unknown"}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            episode.airDate?.let {
                                Text(
                                    text = "Aired: $it",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            episode.overview?.let {
                                Text(
                                    text = it,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }


                // Cast y Crew
                if (!tvShow.aggregateCredits?.cast.isNullOrEmpty() || !tvShow.aggregateCredits?.crew.isNullOrEmpty()) {
                    Text(
                        text = "Cast & Crew",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (!tvShow.aggregateCredits?.cast.isNullOrEmpty()) {
                        Text(
                            text = "Cast",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(tvShow.aggregateCredits?.cast?.take(10) ?: emptyList()) { actor ->
                                CastMemberCard(actor)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (!tvShow.aggregateCredits?.crew.isNullOrEmpty()) {
                        Text(
                            text = "Crew",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(tvShow.aggregateCredits?.crew?.take(10) ?: emptyList()) { crewMember ->
                                CrewMemberCard(crewMember)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Temporadas con tabs
                if (!tvShow.seasons.isNullOrEmpty()) {
                    Text(
                        text = "Seasons",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    SeasonsTabs(seasons = tvShow.seasons)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SeasonsTabs(seasons: List<Season>) {
    val selectedSeasonState = rememberSaveable { mutableStateOf(0) }
    val selectedSeasonIndex = selectedSeasonState.value

    if (seasons.isEmpty()) return

    // Tabs para seleccionar temporada
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(seasons) { index, season ->
            val isSelected = index == selectedSeasonIndex
            Card(
                modifier = Modifier
                    .clickable {
                        selectedSeasonState.value = index
                    }
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = season.name ?: "Season ${season.seasonNumber}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Mostrar episodios de la temporada seleccionada
    val selectedSeason = seasons.getOrNull(selectedSeasonIndex)
    if (selectedSeason != null && !selectedSeason.episodes.isNullOrEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(selectedSeason.episodes) { _, episode ->
                EpisodeCard(episode)
            }
        }
    } else if (selectedSeason != null) {
        Text(
            text = "No episodes available",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun EpisodeCard(episode: Episode) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Miniatura del episodio
            if (!episode.stillPath.isNullOrEmpty()) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w300${episode.stillPath}",
                    contentDescription = episode.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp, 45.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp, 45.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.film),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Información del episodio
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Número del episodio
                Text(
                    text = "Ep. ${episode.episodeNumber} - ${episode.name ?: "Unknown"}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Fecha de aire
                episode.airDate?.let {
                    Text(
                        text = "Aired: $it",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                // Overview del episodio
                episode.overview?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            text = it,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Rating del episodio
            if (episode.voteAverage != null && episode.voteAverage > 0) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${(episode.voteAverage * 10).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            episode.voteAverage < 4 -> Color.Red
                            episode.voteAverage < 7 -> Color.Yellow
                            else -> Color(0xFF2AE98E)
                        }
                    )
                }
            }
        }
    }
}

// Helper para circular rating
@Composable
fun CircularAvgVotes(voteAverage: Double) {
    val votePercentage = (voteAverage * 10).toInt()
    if (votePercentage == 0) return

    val borderColor = when {
        votePercentage < 40 -> Color.Red
        votePercentage < 70 -> Color.Yellow
        else -> Color(0xFF2AE98E)
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color(0xFF18262B), RoundedCornerShape(50))
            .border(3.dp, borderColor, RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$votePercentage%",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun CastMemberCard(actor: org.lanzadera.proyectos.domain.models.tvshow.AggregateCast) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Foto del actor
        if (!actor.profilePath.isNullOrEmpty()) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w300${actor.profilePath}",
                contentDescription = actor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.film),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Nombre del actor
        Text(
            text = actor.name ?: "Unknown",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        // Personaje/Rol
        if (!actor.roles.isNullOrEmpty()) {
            val character = actor.roles.firstOrNull()?.character
            character?.let {
                Text(
                    text = it,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Número de episodios
        actor.episodeCount?.let {
            Text(
                text = "$it episodes",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CrewMemberCard(crewMember: org.lanzadera.proyectos.domain.models.tvshow.AggregateCrew) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Foto del personal
        if (!crewMember.profilePath.isNullOrEmpty()) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w300${crewMember.profilePath}",
                contentDescription = crewMember.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.film),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Nombre
        Text(
            text = crewMember.name ?: "Unknown",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        // Departamento/Trabajo
        if (!crewMember.jobs.isNullOrEmpty()) {
            val job = crewMember.jobs.firstOrNull()?.job
            job?.let {
                Text(
                    text = it,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Número de episodios
        crewMember.episodeCount?.let {
            Text(
                text = "$it episodes",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
