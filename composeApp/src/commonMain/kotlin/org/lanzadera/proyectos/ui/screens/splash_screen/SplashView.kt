package org.lanzadera.proyectos.ui.screens.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.http.ContentType
import kotlinx.coroutines.delay
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.factura
import movieapp.composeapp.generated.resources.unicorn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.screens.login.LoginViewModel

@Composable
@Preview
fun SplashView( nav: NavigationController) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        nav.navigateToLogin()
    }
    Splash()
}


@Composable
fun Splash() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize() // Ocupa toda la pantalla
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Image(
                painter = painterResource(resource = Res.drawable.unicorn),
                contentDescription = "Glide image ",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}