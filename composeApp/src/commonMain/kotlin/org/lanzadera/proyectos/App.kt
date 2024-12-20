package org.lanzadera.proyectos

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.ui.screens.login.LoginView

@Composable
@Preview
fun App() {
    MaterialTheme {
        LoginView()
    }
}


