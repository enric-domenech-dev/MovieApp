package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.new_edge_logo
import org.jetbrains.compose.resources.painterResource
import org.lanzadera.proyectos.ui.models.GameUI
import org.lanzadera.proyectos.navigation.Screen
import org.lanzadera.proyectos.utils.Constants

@Composable
fun GameHeader(
    modifier: Modifier = Modifier,
    game: GameUI,
    onGameClick: (gameId: Int) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                game.id?.let { gameId ->
                    onGameClick(gameId)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (game.hasValidCover) {
                AsyncImage(
                    model = game.coverImageUrl,
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        game.name?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GameSubheader(
    modifier: Modifier = Modifier,
    game: GameUI,
    showMeta: Boolean,
    onGameClick: (gameId: Int) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                game.id?.let { gameId ->
                    onGameClick(gameId)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = game.coverImageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(Res.drawable.new_edge_logo)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (showMeta) {
            // Show rating with icon
            game.rating?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = game.ratingText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        game.name?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
