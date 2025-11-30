package org.lanzadera.proyectos.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.new_edge_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.ui.components.DevelopingDialog
import org.lanzadera.proyectos.ui.components.EmailInput
import org.lanzadera.proyectos.ui.components.FingerPrintAuthentication
import org.lanzadera.proyectos.ui.components.PasswordInput
import org.lanzadera.proyectos.ui.components.PrimaryButton
import org.lanzadera.proyectos.utils.Constants
import org.lanzadera.proyectos.utils.Strings

@Composable
@Preview
fun LoginView(
    nav: NavHostController, vm: LoginViewModel,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    val user by vm.userState.collectAsState()
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf(Strings.Generic.LOADING) }
    var showDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("test@gmail.com") }
    var password by remember { mutableStateOf("1234") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoginSuccessful by vm.isLoginSuccessful.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    var errorMessage by remember { mutableStateOf("") }
    var failedAttempts by remember { mutableStateOf(0) }

    // Observa el estado de login y navega cuando se haya realizado correctamente
    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful == true) {
            nav.navigate(Constants.Screen.Home.route)
        } else {
            // Si el login falla, incrementar el contador de intentos fallidos
            if (failedAttempts < 3) {
                failedAttempts++
                errorMessage = Strings.Auth.LOGIN_FAILED
            } else {
                errorMessage = Strings.Auth.LOGIN_LIMIT_REACHED
            }
        }
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(resource = Res.drawable.new_edge_logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            EmailInput(email, { email = it }, null)

            Spacer(modifier = Modifier.height(8.dp))

            PasswordInput(password, passwordVisible, { password = it }, { passwordVisible = !passwordVisible }, null)

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                 onClick = {
                    // Reiniciar estado de error antes de intentar login
                    if (failedAttempts < 3) {
                        errorMessage = ""
                        vm.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                text = Strings.Auth.LOG_IN,
                description = Strings.Auth.LOG_IN_BUTTON,
                enabled = !isLoading && failedAttempts < 3,
                icon = null
            )

            // Mensaje de carga o error
            if (isLoading) {
                CircularProgressIndicator()
            } else if (isLoginSuccessful == false) {
                Text(errorMessage)
            }

            Spacer(modifier = Modifier.height(8.dp))

            FingerPrintAuthentication(
                 modifier = Modifier
                     .clickable {
                         showDialog = true
                     }
             )
             if (showDialog) {
                DevelopingDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false }
                )
             }

            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.background).weight(1f))

            Text(
                Strings.Auth.DONT_HAVE_ACCOUNT,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodySmall
            )

            PrimaryButton(
                 onClick = { /*navigateToSignIn()*/ },
                 modifier = Modifier.fillMaxWidth(),
                 icon = null,
                text = Strings.Auth.SIGN_IN,
                description = Strings.Auth.SIGN_IN_BUTTON,
                 enabled = true,
             )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
