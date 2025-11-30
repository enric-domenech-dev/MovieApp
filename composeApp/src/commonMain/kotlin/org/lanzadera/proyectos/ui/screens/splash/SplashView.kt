package org.lanzadera.proyectos.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.new_edge_logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.utils.Constants

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashView(
    nav: NavHostController,
    darkTheme: Boolean = false,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    viewModel: SplashViewModel = koinViewModel()
) {
    val isLoadingComplete = viewModel.isLoadingComplete.collectAsState().value

    LaunchedEffect(key1 = isLoadingComplete) {
        if (isLoadingComplete) {
            // Navigate to Home Screen
            nav.navigate(Constants.Screen.Home.route) {
                popUpTo(Constants.Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.new_edge_logo),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize().safeDrawingPadding()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 180.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                CircularProgressIndicator()
            }
        }
    }
}