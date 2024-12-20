package org.lanzadera.proyectos.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import facturas.composeapp.generated.resources.Res
import facturas.composeapp.generated.resources.factura
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.ui.components.EmailInput
import org.lanzadera.proyectos.ui.components.PasswordInput
import org.lanzadera.proyectos.ui.components.PrimaryButton

@Composable
@Preview
fun LoginView(){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            //isError = viewModel.emailError.value
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordInput(
            password = password,
            passwordVisible = passwordVisible,
            onPasswordChange = { password = it },
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            isError = null
            //isError = viewModel.passwordError.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            onClick = { /*navigateToHome()*/ },
            modifier = Modifier.fillMaxWidth(),
            text = "Log In",
            description = "Log In Button",
            enabled = true,
            icon = null
        )


        Spacer(modifier = Modifier.weight(1f))

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