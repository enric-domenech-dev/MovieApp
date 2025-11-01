package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.huella
import movieapp.composeapp.generated.resources.visibility
import movieapp.composeapp.generated.resources.visibility_off

@Composable
fun EmailInput(
    email: String,
    onEmailChange: (String) -> Unit,
    isError: String?
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        isError = isError?.isNotEmpty() ?: false,
    )
    if (isError?.isNotEmpty() == true) {
        Text(
            text = isError,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun PasswordInput(
    password: String,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    isError: String?,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(15.dp),
        isError = isError?.isNotEmpty() ?: false,
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                val iconRes = if (passwordVisible) Res.drawable.visibility_off else Res.drawable.visibility
                val description = if (passwordVisible) "Hide password" else "Show password"
                Image(
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(iconRes),
                    contentDescription = description
                )
            }
        }
    )
    if (isError?.isNotEmpty() == true) {
        Text(
            text = isError,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    icon: DrawableResource?,
    text: String,
    description: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(15.dp),
    ) {
        if (icon != null) {
            Image(
                modifier = Modifier.width(35.dp).height(35.dp),
                painter = painterResource(icon),
                contentDescription = description
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FingerPrintAuthentication(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Authenticate with Biometrics", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Image(painter = painterResource(Res.drawable.huella), contentDescription = "Fingerprint Icon", modifier = Modifier.size(50.dp))
    }
}

@Composable
fun DevelopingDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Info") },
            text = { Text("Functionality under development") },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
            confirmButton = {}
        )
    }
}
