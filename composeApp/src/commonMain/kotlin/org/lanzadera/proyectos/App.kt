package org.lanzadera.proyectos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
internal fun App() {
    MaterialTheme {

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = NavigationController()
            Navigation(navHost = rememberNavController(), navigation = navController)
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()
