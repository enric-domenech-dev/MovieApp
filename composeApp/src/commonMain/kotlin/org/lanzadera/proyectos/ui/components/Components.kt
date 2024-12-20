package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import facturas.composeapp.generated.resources.Res
import facturas.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body2,
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
                val icon =
                    if (passwordVisible) Res.drawable.visibility_off else Res.drawable.visibility
                val description = if (passwordVisible) "Hide password" else "Show password"

                Image(
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(icon),
                    contentDescription = description
                )
            }
        }
    )
    if (isError?.isNotEmpty() == true) {
        Text(
            text = isError,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body2,
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
        if (icon != null){
            Image(
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp),
                painter = painterResource(icon),
                contentDescription = description
            )
        }

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
    }
}