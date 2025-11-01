package org.lanzadera.proyectos.ui.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.ui.components.BookItem

@Composable
fun BookSectionDialog(
    title: String,
    items: List<Book>,
    nav: NavHostController,
    sectionIndex: Int,
    dialogVisible: Boolean,
    onRequestHideContent: () -> Unit,
    onDismissed: () -> Unit,
    animDuration: Int
) {
    Dialog(
        onDismissRequest = { onRequestHideContent() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = dialogVisible,
            enter = fadeIn(animationSpec = tween(animDuration)) + slideInVertically(animationSpec = tween(animDuration)) { it / 4 },
            exit = fadeOut(animationSpec = tween(animDuration)) + slideOutVertically(animationSpec = tween(animDuration)) { it / 4 }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = title, style = MaterialTheme.typography.headlineSmall)
                        IconButton(onClick = { onRequestHideContent() }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Cerrar")
                        }
                    }

                    val gridState = rememberLazyGridState()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        state = gridState,
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = items,
                            key = { index, book ->
                                val idPart = book.id?.toString() ?: book.hashCode().toString()
                                "s${sectionIndex}_${idPart}_$index"
                            }
                        ) { _, book ->
                            BookItem(nav, book)
                        }
                    }
                }
            }
        }
    }

    if (!dialogVisible) {
        LaunchedEffect(dialogVisible) {
            delay(animDuration.toLong())
            onDismissed()
        }
    }
}

