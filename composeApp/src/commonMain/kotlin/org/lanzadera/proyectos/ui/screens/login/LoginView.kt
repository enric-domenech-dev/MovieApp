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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.factura
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DevelopingDialog
import org.lanzadera.proyectos.ui.components.EmailInput
import org.lanzadera.proyectos.ui.components.FingerPrintAuthentication
import org.lanzadera.proyectos.ui.components.PasswordInput
import org.lanzadera.proyectos.ui.components.PrimaryButton

@Composable
@Preview
fun LoginView(
    nav: NavigationController, vm: LoginViewModel
) {
    val user by vm.userState.collectAsState()
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("Loading") }
    var showDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoginSuccessful by vm.isLoginSuccessful.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    // Mensaje de error
    var errorMessage by remember { mutableStateOf("") }

    // Contador de intentos fallidos
    var failedAttempts by remember { mutableStateOf(0) }


    // Observa el estado de login y navega cuando se haya realizado correctamente
    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful == true) {
            nav.navigateToHome()
        } else {
            // Si el login falla, incrementar el contador de intentos fallidos
            if (failedAttempts < 3) {
                failedAttempts++
                errorMessage = "Login fallido: Usuario no encontrado o credenciales inválidas."
            } else {
                errorMessage = "Se ha alcanzado el límite de intentos fallidos."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(resource = Res.drawable.factura),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        EmailInput(
            email = email,
            onEmailChange = { email = it },
            isError = null
            //isError = vm.emailError.value
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordInput(
            password = password,
            passwordVisible = passwordVisible,
            onPasswordChange = { password = it },
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            isError = null
            //isError = vm.passwordError.value
        )

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
            text = "Log In",
            description = "Log In Button",
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
                    // TODO()
                    showDialog = true
                }
        )
        if (showDialog) {
            DevelopingDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onConfirm = { }
            )
        }

        Spacer(modifier = Modifier.background(MaterialTheme.colors.background).weight(1f))

        Text(
            "Don't have an account yet?",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 10.dp),
            style = MaterialTheme.typography.body2
        )

        PrimaryButton(
            onClick = { /*navigateToSignIn()*/ },
            modifier = Modifier.fillMaxWidth(),
            icon = null,
            text = "Sign In",
            description = "Sign In Button",
            enabled = true,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

