package org.lanzadera.proyectos.ui.screens.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.logo_Movira
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
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.logo_Movira),
                        contentDescription = "Logo",
                        modifier = Modifier.size(400.dp).padding(top = 16.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(text = "v1.0.0", style = MaterialTheme.typography.body2)
            }
        }
    )
}