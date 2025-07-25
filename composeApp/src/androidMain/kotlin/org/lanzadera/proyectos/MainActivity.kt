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
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            // Habilitar el auto ocultamiento de las barras del sistema
            EnableAutoHideSystemBars()

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

    @Composable
    private fun EnableAutoHideSystemBars() {
        val view = LocalView.current
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            // Asegura que el contenido se dibuje detrás de las barras del sistema
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            // Ocultar tanto la barra de estado como la barra de navegación
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            // Asegura que las barras del sistema puedan mostrarse con un deslizamiento
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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