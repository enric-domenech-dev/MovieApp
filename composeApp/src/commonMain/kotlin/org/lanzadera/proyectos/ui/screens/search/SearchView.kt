package org.lanzadera.proyectos.ui.screens.search

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.UIClass.NewspaperFrontPage

@Composable
@Preview
fun SearchView(
    nav: NavigationController, vm: SearchViewModel,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    Scaffold {
        NewspaperFrontPage()
    }
}