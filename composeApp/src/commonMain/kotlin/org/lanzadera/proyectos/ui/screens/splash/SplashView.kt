package org.lanzadera.proyectos.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.full_background_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.utils.Constants

@Composable
@Preview
fun SplashView(
    nav: NavHostController,
    darkTheme: Boolean = false,
    selectedTheme: AppTheme = AppTheme.SYSTEM
) {
    LaunchedEffect(key1 = true) {
        // Delay standard simulating avg load time
        delay(2000)
        // Navigate to Home Screen
        nav.navigate(Constants.Screen.Home.route)
    }
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.full_background_logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize().safeDrawingPadding()
            )
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