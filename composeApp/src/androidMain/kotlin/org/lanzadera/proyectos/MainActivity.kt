package org.lanzadera.proyectos

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge() // Habilita el modo edge-to-edge

        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                val window = (view.context as Activity).window
                WindowCompat.setDecorFitsSystemWindows(window, false)
                val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            // Usando el tema del sistema por defecto (claro/oscuro automático)
            App(
                selectedTheme = AppTheme.SYSTEM,
                darkTheme = isSystemInDarkTheme()
            )
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