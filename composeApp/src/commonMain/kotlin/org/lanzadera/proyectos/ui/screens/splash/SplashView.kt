package org.lanzadera.proyectos.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.Splash
import movieapp.composeapp.generated.resources.Splash_retro
import movieapp.composeapp.generated.resources.film
import movieapp.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.navigation.NavigationController

@Composable
@Preview
fun SplashView(
    nav: NavigationController,
    darkTheme: Boolean = false,
    selectedTheme: AppTheme = AppTheme.SYSTEM
) {
    LaunchedEffect(key1 = true) {
        delay(800)
        nav.navigateToHome()
    }
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {

                when (selectedTheme) {

                    AppTheme.SYSTEM -> {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(350.dp).padding(top = 16.dp),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }

                    AppTheme.DARK -> {
                        Image(
                            painter = painterResource(Res.drawable.film),
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop

                        )
                    }

                    AppTheme.LIGHT -> {
                        Image(
                            painter = painterResource(Res.drawable.Splash),
                            contentDescription = "Logo",
                            modifier = Modifier.size(400.dp).padding(top = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    AppTheme.NEON -> {
                        // TODO: change image to neon theme
                        Image(
                            painter = painterResource(Res.drawable.film),
                            contentDescription = "Logo",
                            modifier = Modifier.size(400.dp).padding(top = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    AppTheme.RETRO -> {
                        Image(
                            painter = painterResource(Res.drawable.Splash_retro),
                            contentDescription = "Logo",
                            modifier = Modifier.size(400.dp).padding(top = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "v1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}