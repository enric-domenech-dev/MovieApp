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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationStore
import org.lanzadera.proyectos.utils.Constants

@Composable
fun MovieItem(
    nav: NavHostController,
    movie: Movie,
    modifier: Modifier = Modifier.wrapContentHeight(),
    showMeta: Boolean = true
) {
    Column(
        modifier = modifier
            .clickable {
                NavigationStore.selectedMovie = movie
                movie.id?.let { movieId ->
                    nav.navigate(Constants.Screen.MovieDetail.createRoute(movieId))
                }
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )
        }

        if (showMeta) {
            Spacer(modifier = Modifier.height(6.dp))
            movie.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val formattedDate = remember(movie.releaseDate) {
                movie.releaseDate?.let {
                    val date = LocalDate.parse(it)
                    "${date.dayOfMonth} ${date.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }} ${date.year}"
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
fun MovieHeader(modifier: Modifier = Modifier, nav: NavHostController, movie: Movie) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedMovie = movie
                movie.id?.let { movieId ->
                    nav.navigate(Constants.Screen.MovieDetail.createRoute(movieId))
                }
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        movie.title?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        movie.releaseDate?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun MovieSubheader(modifier: Modifier = Modifier, nav: NavHostController, movie: Movie, showMeta: Boolean) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedMovie = movie
                movie.id?.let { movieId ->
                    nav.navigate(Constants.Screen.MovieDetail.createRoute(movieId))
                }
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )
        }
        if (showMeta) {
            Spacer(modifier = Modifier.height(6.dp))
            movie.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            movie.releaseDate?.let {
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
fun MovieDetail(movie: Movie?, modifier: Modifier = Modifier) {
    val isFavorite = rememberSaveable { mutableStateOf(false) }
    if (movie == null) return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        item {
            // Backdrop
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.backdropPath}",
                    contentDescription = movie.title,
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
                movie.releaseDate?.let {
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

                CircularAvgVotes(movie)

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
                    shape = CircleShape,
                )
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Título
                movie.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Info línea: estado, duración
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    movie.status?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    movie.runtime?.let {
                        Text(
                            text = "${it}min",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Géneros
                if (!movie.genres.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        movie.genres.forEach { genre ->
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
                movie.overview?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tagline
                movie.tagline?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            text = "\"$it\"",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Información financiera
                if (movie.budget != null || movie.revenue != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (movie.budget != null && movie.budget > 0) {
                            Text(
                                text = "Budget: \$${formatNumber(movie.budget)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        if (movie.revenue != null && movie.revenue > 0) {
                            Text(
                                text = "Revenue: \$${formatNumber(movie.revenue)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Países de origen
                if (!movie.productionCountries.isNullOrEmpty()) {
                    Text(
                        text = "Countries: ${movie.productionCountries.map { it.name }.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Idiomas
                if (!movie.spokenLanguages.isNullOrEmpty()) {
                    Text(
                        text = "Languages: ${
                            movie.spokenLanguages.map { it.englishName ?: it.name }.joinToString(", ")
                        }",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Productoras
                if (!movie.productionCompanies.isNullOrEmpty()) {
                    Text(
                        text = "Production: ${movie.productionCompanies.map { it.name }.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Colección
                movie.belongsToCollection?.let { collection ->
                    Text(
                        text = "Part of Collection",
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!collection.posterPath.isNullOrEmpty()) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w200${collection.posterPath}",
                                    contentDescription = collection.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp, 75.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                )
                            }
                            Text(
                                text = collection.name ?: "Unknown Collection",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Cast y Crew
                if (!movie.aggregateCredits?.cast.isNullOrEmpty() || !movie.aggregateCredits?.crew.isNullOrEmpty()) {
                    Text(
                        text = "Cast & Crew",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (!movie.aggregateCredits?.cast.isNullOrEmpty()) {
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
                            items(movie.aggregateCredits?.cast?.take(10) ?: emptyList()) { actor ->
                                MovieCastMemberCard(actor)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (!movie.aggregateCredits?.crew.isNullOrEmpty()) {
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
                            items(movie.aggregateCredits?.crew?.take(10) ?: emptyList()) { crewMember ->
                                MovieCrewMemberCard(crewMember)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CircularAvgVotes(movie: Movie) {
    val votePercentage = (movie.voteAverage.toDouble() * 10).toInt()
    if (votePercentage == 0) return
    val borderColor = when {
        votePercentage < 40 -> Color.Red
        votePercentage < 70 -> Color.Yellow
        else -> Color(0xFF2AE98E)
    }
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color(0xFF18262B), CircleShape)
            .border(3.dp, borderColor, CircleShape),
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
fun MovieCastMemberCard(actor: org.lanzadera.proyectos.domain.models.tvshow.AggregateCast) {
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
    }
}

@Composable
fun MovieCrewMemberCard(crewMember: org.lanzadera.proyectos.domain.models.tvshow.AggregateCrew) {
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
    }
}

@Composable
fun MovieInfoTabContent(movie: Movie?, modifier: Modifier = Modifier) {
    if (movie == null) return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                // Title
                movie.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Info: status, runtime
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    movie.status?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    movie.runtime?.let {
                        Text(
                            text = "${it}min",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Genres
                if (!movie.genres.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        movie.genres.forEach { genre ->
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
                movie.overview?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tagline
                movie.tagline?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            text = "\"$it\"",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Financial Info
                if (movie.budget != null || movie.revenue != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (movie.budget != null && movie.budget > 0) {
                            Text(
                                text = "Budget: \$${formatNumber(movie.budget)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        if (movie.revenue != null && movie.revenue > 0) {
                            Text(
                                text = "Revenue: \$${formatNumber(movie.revenue)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Countries
                if (!movie.productionCountries.isNullOrEmpty()) {
                    Text(
                        text = "Countries: ${movie.productionCountries.map { it.name }.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Languages
                if (!movie.spokenLanguages.isNullOrEmpty()) {
                    Text(
                        text = "Languages: ${
                            movie.spokenLanguages.map { it.englishName ?: it.name }.joinToString(", ")
                        }",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Production Companies
                if (!movie.productionCompanies.isNullOrEmpty()) {
                    Text(
                        text = "Production: ${movie.productionCompanies.map { it.name }.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Collection
                movie.belongsToCollection?.let { collection ->
                    Text(
                        text = "Part of Collection",
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!collection.posterPath.isNullOrEmpty()) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w200${collection.posterPath}",
                                    contentDescription = collection.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp, 75.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                )
                            }
                            Text(
                                text = collection.name ?: "Unknown Collection",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun MovieCreditsTab(movie: Movie?, modifier: Modifier = Modifier) {
    if (movie == null) return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        // Cast
        if (!movie.aggregateCredits?.cast.isNullOrEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cast",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movie.aggregateCredits?.cast?.take(10) ?: emptyList()) { actor ->
                        MovieCastMemberCard(actor)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Crew
        if (!movie.aggregateCredits?.crew.isNullOrEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Crew",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movie.aggregateCredits?.crew?.take(10) ?: emptyList()) { crewMember ->
                        MovieCrewMemberCard(crewMember)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Helper function to format numbers with thousand separators
fun formatNumber(number: Int): String {
    return number.toString().reversed().chunked(3).joinToString(",").reversed()
}
