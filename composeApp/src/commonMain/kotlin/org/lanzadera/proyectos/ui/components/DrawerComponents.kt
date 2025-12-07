package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.new_edge_logo
import org.jetbrains.compose.resources.painterResource
import org.lanzadera.proyectos.BuildConfig
import org.lanzadera.proyectos.navigation.Screen
import org.lanzadera.proyectos.utils.Constants.Dimensions.BOTTOM_NAV_BAR_HEIGHT
import org.lanzadera.proyectos.utils.Strings

@Composable
fun DrawerAppBar(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    drawerEnabled: Boolean = false,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit,
    content: @Composable () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (drawerEnabled) {
        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            drawerContent = {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = BOTTOM_NAV_BAR_HEIGHT)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .safeDrawingPadding()
                                .padding(top = 16.dp)
                                .padding(16.dp)
                        ) {
                                Image(
                                    painter = painterResource(Res.drawable.new_edge_logo),
                                    contentDescription = "App Logo",
                                    modifier = Modifier
                                        .size(264.dp)
                                        .padding(12.dp),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomBottomAppBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(
                                text = "v${BuildConfig.APP_VERSION}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(Strings.Menu.PROFILE) },
                            onClick = { onNavigateToSearch() },
                            leadingIcon = {
                                Icon(Icons.Outlined.Person, contentDescription = null)
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(Strings.Settings.THEME) },
                            onClick = {
                                onNavigateToSettings()
                                scope.launch { drawerState.close() }
                            },
                            leadingIcon = {
                                Icon(Icons.Outlined.Settings, contentDescription = null)
                            }
                        )
                    }

                    HorizontalDivider()

                    DropdownMenuItem(
                        text = { Text("Enviar Comentarios") },
                        onClick = { /* Do something... */ },
                        leadingIcon = {
                            Icon(Icons.Outlined.Email, contentDescription = null)
                        },
                        trailingIcon = {
                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                        }
                    )

                    HorizontalDivider()

                    DropdownMenuItem(
                        text = { Text("Ayuda") },
                        onClick = { /* Do something... */ },
                        leadingIcon = {
                            Icon(Icons.Outlined.Info, contentDescription = null)
                        },
                        trailingIcon = {
                            Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    DropdownMenuItem(
                        text = { Text(Strings.Settings.ABOUT) },
                        onClick = { /* Do something... */ },
                        leadingIcon = {
                            Icon(Icons.Outlined.Info, contentDescription = null)
                        },
                    )

                    DropdownMenuItem(
                        text = { Text(Strings.Settings.LOGOUT) },
                        onClick = { showDialog = true },
                        leadingIcon = {
                            Icon(
                                Icons.AutoMirrored.Outlined.ExitToApp,
                                contentDescription = "Botón de cerrar sesión"
                            )
                        }
                    )
                    LogoutConfirmationDialog(
                        showDialog = showDialog,
                        onDismiss = { showDialog = false },
                        onConfirm = { navViewModel.navigate(Screen.Login) }
                    )
                }
            },
            gesturesEnabled = true,
            content = content
        )
    } else {
        // When drawer is disabled, just show content without the drawer wrapper
        content()
    }
}

@Composable
fun CustomBottomAppBar(
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable (RowScope) -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color,
    contentColor: Color
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineSmall) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
            navigationIconContentColor = contentColor
        )
    )
}

@Composable
fun LogoutConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirmation") },
            text = { Text("Are you sure you want to log out?") },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No", color = lerp(Color.Green, Color.Black, 0.35f))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
                    onConfirm()
                }) {
                    Text(
                        "Yes",
                        modifier = Modifier.padding(horizontal = 40.dp),
                        color = lerp(Color.Red, Color.Black, 0.35f)
                    )
                }
            }
        )
    }
}
