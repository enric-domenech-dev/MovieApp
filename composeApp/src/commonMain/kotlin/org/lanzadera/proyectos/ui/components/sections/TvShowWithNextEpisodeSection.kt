package org.lanzadera.proyectos.ui.components.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.lanzadera.proyectos.ui.models.TvShowWithNextEpisodeUI
import org.lanzadera.proyectos.ui.components.TvShowHeaderWithNextEpisode
import org.lanzadera.proyectos.ui.screens.home.SectionMode

@Composable
fun TvShowWithNextEpisodeSection(
    title: String,
    items: List<TvShowWithNextEpisodeUI>,
    nav: NavHostController,
    sectionIndex: Int = 0,
    mode: SectionMode = SectionMode.HEADER,
    showTitle: Boolean = true
) {
    val visibleItems = remember(items) { items.take(12) }

    Column(modifier = Modifier.fillMaxWidth().padding(top = if (showTitle) 8.dp else 0.dp)) {
        if (showTitle) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (items.size > 12) {
                    Text(
                        text = "VER MÁS... (${items.size})",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.clickable { /* TODO: implementar diálogo */ }
                    )
                }
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val spacing = 16.dp
            val horizontalPadding = 16.dp
            val available = maxWidth - horizontalPadding
            val baseWidth = (available - spacing) / 2f
            val headerWidth = baseWidth
            val rowContentPadding = PaddingValues(horizontal = 8.dp)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.Top,
                contentPadding = rowContentPadding,
                modifier = Modifier.fillMaxWidth().padding(top = if (showTitle) 8.dp else 0.dp)
            ) {
                itemsIndexed(
                    items = visibleItems,
                    key = { index, item ->
                        val idPart = item.tvShow.id?.toString() ?: item.tvShow.hashCode().toString()
                        "s${sectionIndex}_${idPart}_$index"
                    }
                ) { _, item ->
                    TvShowHeaderWithNextEpisode(
                        modifier = Modifier.width(headerWidth),
                        nav = nav,
                        tvShowWithNext = item
                    )
                }
            }
        }
    }
}
