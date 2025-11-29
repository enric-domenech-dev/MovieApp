package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCast
import org.lanzadera.proyectos.domain.models.tvshow.AggregateCrew
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
                tvShow.id?.let { tvShowId ->
                    nav.navigate(Constants.Screen.SeriesDetail.createRoute(tvShowId))
                }
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
                tvShow.id?.let { tvShowId ->
                    nav.navigate(Constants.Screen.SeriesDetail.createRoute(tvShowId))
                }
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
                tvShow.id?.let { tvShowId ->
                    nav.navigate(Constants.Screen.SeriesDetail.createRoute(tvShowId))
                }
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
fun CastMemberCard(actor: org.lanzadera.proyectos.domain.models.tvshow.AggregateCast) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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

        Text(
            text = actor.name ?: "Unknown",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

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

        Text(
            text = crewMember.name ?: "Unknown",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

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

@OptIn(ExperimentalLayoutApi::class)

@Composable
fun SeriesInfoTab(tvShow: TvShow?, modifier: Modifier = Modifier) {
    if (tvShow == null) return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección: Resumen general - Stat Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                tvShow.status?.let {
                    StatCard(label = "Estado", value = it, modifier = Modifier.weight(1f))
                }
                tvShow.numberOfSeasons?.let {
                    StatCard(label = "Temporadas", value = it.toString(), modifier = Modifier.weight(1f))
                }
                tvShow.numberOfEpisodes?.let {
                    StatCard(label = "Episodios", value = it.toString(), modifier = Modifier.weight(1f))
                }
            }
        }

        // Sección: Géneros
        item {
            if (!tvShow.genres.isNullOrEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Géneros",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tvShow.genres.forEach { genre ->
                            AssistChip(
                                onClick = { /* no-op */ },
                                label = {
                                    Text(
                                        text = genre.name.orEmpty(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                modifier = Modifier.heightIn(min = 32.dp)
                            )
                        }
                    }
                }
            }
        }

        // Sección: Descripción
        item {
            tvShow.overview?.let { overview ->
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(14.dp),
                        )
                    }
                }
            }
        }

        // Sección: Plataformas
        item {
            if (!tvShow.networks.isNullOrEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Disponible en",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tvShow.networks.forEach { network ->
                            Column(
                                modifier = Modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (!network.logoPath.isNullOrEmpty()) {
                                    Surface(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        color = MaterialTheme.colorScheme.surfaceContainerLow
                                    ) {
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w200${network.logoPath}",
                                            contentDescription = network.name,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

/* ---------- Helpers Material 3 ---------- */

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.heightIn(min = 88.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }
    }
}

@Composable
fun SeriesSeasonsTab(
    tvShow: TvShow?,
    watchedEpisodes: Set<String>,
    onEpisodeToggle: (Int, Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tvShow == null || tvShow.seasons.isNullOrEmpty()) return

    LazyColumn(
        modifier = modifier.fillMaxWidth().navigationBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Last Episode - Highlighted Card
        if (tvShow.lastEpisodeToAir != null) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Último episodio",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )

                        tvShow.lastEpisodeToAir.let { episode ->
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "T${
                                        episode.seasonNumber?.toString()?.padStart(2, '0')
                                    } • E${
                                        episode.episodeNumber?.toString()?.padStart(2, '0')
                                    } - ${episode.name ?: "Desconocido"}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                                episode.airDate?.let {
                                    Text(
                                        text = "Emitido: $it",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                episode.overview?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Temporadas",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Seasons List
        items(tvShow.seasons.size) { index ->
            SeasonListItem(
                season = tvShow.seasons[index],
                watchedEpisodes = watchedEpisodes,
                onEpisodeToggle = onEpisodeToggle
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
fun SeasonListItem(
    season: Season,
    watchedEpisodes: Set<String>,
    onEpisodeToggle: (Int, Int, Boolean) -> Unit
) {
    val isExpanded = rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { isExpanded.value = !isExpanded.value },
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = season.name ?: "Temporada ${season.seasonNumber}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${season.episodes?.size ?: 0} episodios",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = if (isExpanded.value) Icons.AutoMirrored.Rounded.ArrowForward else Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isExpanded.value && !season.episodes.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    season.episodes.forEach { episode ->
                        EpisodeListItem(
                            episode = episode,
                            isWatched = episode.seasonNumber?.let { sn ->
                                episode.episodeNumber?.let { en ->
                                    watchedEpisodes.contains("$sn-$en")
                                }
                            } ?: false,
                            onToggle = { isWatched ->
                                episode.seasonNumber?.let { sn ->
                                    episode.episodeNumber?.let { en ->
                                        onEpisodeToggle(sn, en, isWatched)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeListItem(episode: Episode, isWatched: Boolean, onToggle: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isWatched,
                onCheckedChange = { onToggle(it) },
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 2.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Ep. ${episode.episodeNumber} - ${episode.name ?: "Desconocido"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.3.sp
                )
                episode.airDate?.let {
                    Text(
                        text = "Emitido: $it",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 9.sp
                    )
                }
                episode.overview?.let {
                    if (it.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            if (episode.voteAverage != null && episode.voteAverage > 0) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)),
                    color = when {
                        episode.voteAverage < 4 -> Color(0xFFEF5350)
                        episode.voteAverage < 7 -> Color(0xFFFFA726)
                        else -> Color(0xFF66BB6A)
                    }.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${(episode.voteAverage * 10).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            episode.voteAverage < 4 -> Color(0xFFEF5350)
                            episode.voteAverage < 7 -> Color(0xFFFFA726)
                            else -> Color(0xFF66BB6A)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SeriesCreditsTab(tvShow: TvShow?, modifier: Modifier = Modifier) {
    if (tvShow == null) return

    LazyColumn(
        modifier = modifier.fillMaxWidth().navigationBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Created By Section
        if (!tvShow.createdBy.isNullOrEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Creado por",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = tvShow.createdBy.map { it.name }.joinToString(", "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }
        }

        // Cast Section
        if (!tvShow.aggregateCredits?.cast.isNullOrEmpty()) {
            item {
                Text(
                    text = "Elenco",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    items(tvShow.aggregateCredits?.cast?.take(10) ?: emptyList()) { actor ->
                        CastMemberCardModern(actor)
                    }
                }
            }
        }

        // Crew Section
        if (!tvShow.aggregateCredits?.crew.isNullOrEmpty()) {
            item {
                Text(
                    text = "Equipo técnico",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    items(tvShow.aggregateCredits?.crew?.take(10) ?: emptyList()) { crewMember ->
                        CrewMemberCardModern(crewMember)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
fun CastMemberCardModern(actor: AggregateCast) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (!actor.profilePath.isNullOrEmpty()) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original${actor.profilePath}",
                    contentDescription = actor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.film),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text = actor.name ?: "Unknown",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        if (!actor.roles.isNullOrEmpty()) {
            val character = actor.roles.firstOrNull()?.character
            character?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun CrewMemberCardModern(crewMember: AggregateCrew) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (!crewMember.profilePath.isNullOrEmpty()) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original${crewMember.profilePath}",
                    contentDescription = crewMember.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.film),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text = crewMember.name ?: "Unknown",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        if (!crewMember.jobs.isNullOrEmpty()) {
            val job = crewMember.jobs.firstOrNull()?.job
            job?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun TvShowHeaderWithNextEpisode(
    modifier: Modifier = Modifier,
    nav: NavHostController,
    tvShowWithNext: org.lanzadera.proyectos.domain.models.tvshow.TvShowWithNextEpisode
) {
    val tvShow = tvShowWithNext.tvShow
    val nextEpisode = tvShowWithNext.nextEpisode

    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedTvShow = tvShow
                tvShow.id?.let { tvShowId ->
                    nav.navigate(Constants.Screen.SeriesDetail.createRoute(tvShowId))
                }
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

        // Solo mostrar información del próximo episodio, sin título de serie
        nextEpisode?.let { episode ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = episode.episodeCode,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = episode.displayText,
                fontSize = 14.sp,
                color = if (episode.isAired) Color(0xFF4CAF50) else Color(0xFFFF9800),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
