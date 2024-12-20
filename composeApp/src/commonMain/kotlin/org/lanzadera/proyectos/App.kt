package org.lanzadera.proyectos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.navigation.Navigation
import org.lanzadera.proyectos.navigation.NavigationController

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = NavigationController()
            Navigation(navHost = rememberNavController(), navigation = navController)
        }
    }
}


