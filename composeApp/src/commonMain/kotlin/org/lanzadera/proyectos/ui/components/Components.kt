package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import facturas.composeapp.generated.resources.Res
import facturas.composeapp.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.lanzadera.proyectos.navigation.NavigationController


@Composable
fun DrawerAppBar(
    navViewModel: NavigationController,
    pageTitle: String,
    actionButton: @Composable () -> Unit,
    screenContent: @Composable () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Image",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        "User Name".uppercase(),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.body2
                    )
                }

                Divider()

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    DropdownMenuItem(
                        content = {
                            Row {
                                Icon(Icons.Outlined.Person, contentDescription = null)
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Profile")

                            }

                        },
                        onClick = { /* Do something... */ }
                    )

                    DropdownMenuItem(
                        content = {
                            Row {
                                Icon(Icons.Outlined.Settings, contentDescription = null)
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Settings")
                            }
                        },
                        onClick = { /* Do something... */ }
                    )
                }

                Divider()

                DropdownMenuItem(
                    content = {
                        Row {
                            Icon(Icons.Outlined.Email, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Send Feedback")
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                        }

                    },
                    onClick = { /* Do something... */ }
                )

                Divider()

                DropdownMenuItem(
                    content = {
                        Row {
                            Icon(Icons.Outlined.Info, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Help")
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null)
                        }
                    },
                    onClick = { /* Do something... */ }
                )

                DropdownMenuItem(
                    content = {
                        Row {
                            Icon(Icons.Outlined.Info, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("About")
                        }
                    },
                    onClick = { /* Do something... */ }
                )

                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    content = { /* Add content */ },
                    onClick = { /* Do something... */ }
                )




                DropdownMenuItem(
                    content = {
                        LogoutButton(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            enabled = true,
                        )
                        LogoutConfirmationDialog(
                            showDialog = showDialog,
                            onDismiss = { showDialog = false },
                            onConfirm = { navViewModel.navigateToLogin() }
                        )
                    },
                    onClick = { /* Do something... */ }
                )
            }
        },
        gesturesEnabled = true,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    title = pageTitle,
                    contentColor = MaterialTheme.colors.onBackground,
                    backgroundColor = MaterialTheme.colors.background,
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navViewModel.navigateBack() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Configuration"
                            )
                        }
                    },
                )
            },
            bottomBar = {
                CustomBottomAppBar(
                    containerColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        SingleChoiceSegmentedButtonAlternativeExample(

                        )
                    }
                )
            },
            floatingActionButton = { actionButton() },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    screenContent()
                }
            }
        )
    }
}

@Composable
fun CustomBottomAppBar(
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
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

@Composable
fun SingleChoiceSegmentedButtonAlternative(
    modifier: Modifier = Modifier,
    options: List<String> = listOf("User", "Shop", "Home", "Game", "News"),
    initialSelectedIndex: Int = 2
) {
    var selectedIndex by remember { mutableIntStateOf(initialSelectedIndex) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        options.forEachIndexed { index, label ->

            Box(

                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(height = 32.dp, width = 72.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == selectedIndex) MaterialTheme.colors.background
                        else MaterialTheme.colors.primary
                    )
                    .clickable { selectedIndex = index }

            ) {
                Text(
                    text = label.uppercase(),
                    color = if (index == selectedIndex) MaterialTheme.colors.primary
                    else MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.align(Alignment.Center)
                )

            }


            /*Box(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .background(
                        if (index == selectedIndex) MaterialTheme.colors.background
                        else MaterialTheme.colors.onBackground
                    )
                    .clickable { selectedIndex = index }
                    .border(
                        BorderStroke(
                            1.dp,
                            if (index == selectedIndex) MaterialTheme.colors.primary
                            else Color.Transparent
                        ),
                    )
            ) {
                Text(
                    text = label.uppercase(),
                    color = if (index == selectedIndex) MaterialTheme.colors.primary
                    else MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

             */
        }
    }
}

@Composable
fun SingleChoiceSegmentedButtonAlternativeExample() {
    SingleChoiceSegmentedButtonAlternative(
        options = listOf("User", "Shop", "Home", "Game", "News"),
        initialSelectedIndex = 2
    )
}

@Composable
fun CustomTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color,
    contentColor: Color
) {
    TopAppBar(
        elevation = (-1).dp,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = title, style = MaterialTheme.typography.h6)
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
    )
}

@Composable
fun DropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf(items[0]) }

    Box(modifier = Modifier.wrapContentSize()) {
        // Button to open the DropdownMenu
        Button(onClick = { expanded = true }) {
            Text(text = selectedOption)
        }

        // DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = item
                        expanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun FingerPrintAuthentication(
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Authenticate with Biometrics",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.body2,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(Res.drawable.huella),
            contentDescription = "Fingerprint Icon",
            modifier = Modifier.size(50.dp)

        )
    }
}

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
        modifier = Modifier
            .fillMaxWidth(),
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
        if (icon != null) {
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


@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Red.copy(alpha = 0.7f),
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.elevation(15.dp)
    ) {
        Icon(
            Icons.AutoMirrored.Outlined.ExitToApp,
            contentDescription = "log out icon button",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Log Out", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
    }
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

@Composable
fun DevelopingDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
            text = { Text("Functionality under development") },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close Dialog")
                }
            },
            confirmButton = { }
        )
    }
}