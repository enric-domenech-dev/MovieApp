package org.lanzadera.proyectos.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.ui.models.BookUI
import org.lanzadera.proyectos.ui.components.BookDetail
import org.lanzadera.proyectos.ui.components.CustomTopAppBar
import androidx.navigation.NavHostController

@Composable
@Preview
fun BookDetailView(
    nav: NavHostController,
    book: BookUI?,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        floatingActionButtonPosition =
            FabPosition.EndOverlay,
        floatingActionButton = {
            // no-op
        },
        topBar = {
            CustomTopAppBar(
                title = book?.title ?: "",
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(imageVector = Icons.Outlined.Info, contentDescription = "Info")
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        },
        content = { paddingValue ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValue)) {
                BookDetail(book = book)
            }
        }
    )
}
