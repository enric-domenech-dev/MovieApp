package org.lanzadera.proyectos

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.navigation.Navigation
import org.lanzadera.proyectos.navigation.NavigationController

// Enum para los diferentes temas disponibles
enum class AppTheme {
    SYSTEM,  // Sigue el tema del sistema (claro/oscuro)
    LIGHT,   // Siempre tema claro
    DARK,    // Siempre tema oscuro
    NEON,    // Tema neon
    RETRO    // Tema retro
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
internal fun App(
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    val colors: ColorScheme = when (selectedTheme) {
        AppTheme.SYSTEM -> if (darkTheme) DarkColorPalette else LightColorPalette
        AppTheme.LIGHT -> LightColorPalette
        AppTheme.DARK -> DarkColorPalette
        AppTheme.NEON -> NeonColorPalette
        AppTheme.RETRO -> RetroColorPalette
    }

    MaterialTheme(colorScheme = colors) {

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = NavigationController()
            Navigation(
                navHost = rememberNavController(),
                navigation = navController,
                selectedTheme = selectedTheme,
                darkTheme = darkTheme
            )
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()