package org.lanzadera.proyectos.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.lanzadera.proyectos.utils.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingView(nav: NavHostController, vm: SettingsViewModel, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = { Text(Strings.Settings.THEME) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(16.dp))
            Text(text = "Aquí se implementará la pantalla de Settings.", style = MaterialTheme.typography.bodyMedium)

            // Use the nav and vm params minimally so they are not considered unused warnings
            Text(text = "Ruta actual: ${nav.currentBackStackEntry?.destination?.route ?: "-"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "VM: ${vm::class.simpleName}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
