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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lanzadera.proyectos.ui.components.BookHeader
import org.lanzadera.proyectos.ui.components.BookSubheader
import org.lanzadera.proyectos.ui.components.dialogs.BookSectionDialog
import org.lanzadera.proyectos.ui.models.BookUI
import org.lanzadera.proyectos.ui.screens.home.SectionMode

@Composable
fun BookSection(
    title: String,
    items: List<BookUI>,
    onBookClick: (bookId: String) -> Unit,
    sectionIndex: Int = 0,
    mode: SectionMode = SectionMode.HEADER
) {
    val visibleItems = remember(items) { items.take(12) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogContentVisible by remember { mutableStateOf(false) }
    val animDuration = 320 // ms

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "VER MÁS... (${items.size})",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable {
                    showDialog = true
                    dialogContentVisible = true
                }
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val spacing = 16.dp
            val horizontalPadding = 16.dp
            val available = maxWidth - horizontalPadding
            val baseWidth = (available - spacing) / 2f
            val headerWidth = baseWidth
            val subWidth = baseWidth * 0.65f
            val rowContentPadding = PaddingValues(horizontal = 8.dp)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.Top,
                contentPadding = rowContentPadding,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                itemsIndexed(
                    items = visibleItems,
                    key = { index, book ->
                        val idPart = book.id?.toString() ?: book.hashCode().toString()
                        "s${sectionIndex}_${idPart}_$index"
                    }
                ) { _, book ->
                    val itemModifier =
                        if (mode == SectionMode.HEADER) Modifier.width(headerWidth) else Modifier.width(subWidth)
                    when (mode) {
                        SectionMode.HEADER -> BookHeader(
                            modifier = itemModifier,
                            book = book,
                            onBookClick = onBookClick
                        )

                        SectionMode.SUBHEADER_SHOW_META -> BookSubheader(
                            modifier = itemModifier,
                            book = book,
                            showMeta = true,
                            onBookClick = onBookClick
                        )

                        SectionMode.SUBHEADER_HIDE_META -> BookSubheader(
                            modifier = itemModifier,
                            book = book,
                            showMeta = false,
                            onBookClick = onBookClick
                        )
                    }
                }
            }

            if (showDialog) {
                BookSectionDialog(
                    title = title,
                    items = items,
                    onBookClick = onBookClick,
                    sectionIndex = sectionIndex,
                    dialogVisible = dialogContentVisible,
                    onRequestHideContent = { dialogContentVisible = false },
                    onDismissed = { showDialog = false },
                    animDuration = animDuration
                )
            }
        }
    }
}

