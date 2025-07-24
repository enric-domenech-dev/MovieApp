package org.lanzadera.proyectos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Usando el tema del sistema por defecto (claro/oscuro automático)
            App(
                selectedTheme = AppTheme.SYSTEM, // Cambia a LIGHT, DARK, NEON o RETRO según sea necesario
                darkTheme = isSystemInDarkTheme()
            )

            // Otras opciones que puedes usar:
            // App(selectedTheme = AppTheme.LIGHT)     // Siempre tema claro
            // App(selectedTheme = AppTheme.DARK)      // Siempre tema oscuro
            // App(selectedTheme = AppTheme.NEON)      // Tema neon
            // App(selectedTheme = AppTheme.RETRO)     // Tema retro
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview con tema claro
    App(selectedTheme = AppTheme.LIGHT)
}

@Preview
@Composable
fun AppAndroidPreviewDark() {
    // Preview con tema oscuro
    App(selectedTheme = AppTheme.DARK)
}

@Preview
@Composable
fun AppAndroidPreviewNeon() {
    // Preview con tema neon
    App(selectedTheme = AppTheme.NEON)
}

@Preview
@Composable
fun AppAndroidPreviewRetro() {
    // Preview con tema retro
    App(selectedTheme = AppTheme.RETRO)
}