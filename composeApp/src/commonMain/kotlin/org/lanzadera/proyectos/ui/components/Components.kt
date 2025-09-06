package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.datetime.LocalDate
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.film
import movieapp.composeapp.generated.resources.guardado
import movieapp.composeapp.generated.resources.guardar
import movieapp.composeapp.generated.resources.huella
import movieapp.composeapp.generated.resources.visibility
import movieapp.composeapp.generated.resources.visibility_off
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.lanzadera.proyectos.BuildConfig
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController


@Composable
fun DrawerAppBar(
    navViewModel: NavigationController,
    drawerState: androidx.compose.material3.DrawerState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
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
                        style = MaterialTheme.typography.headlineSmall
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
                        text = { Text("Profile") },
                        onClick = { navViewModel.navigateToSearch() },
                        leadingIcon = {
                            Icon(Icons.Outlined.Person, contentDescription = null)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = { /* Do something... */ },
                        leadingIcon = {
                            Icon(Icons.Outlined.Settings, contentDescription = null)
                        }
                    )
                }

                Divider()

                DropdownMenuItem(
                    text = { Text("Send Feedback") },
                    onClick = { /* Do something... */ },
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, contentDescription = null)
                    },
                    trailingIcon = {
                        Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text("Help") },
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
                    text = { Text("About") },
                    onClick = { /* Do something... */ },
                    leadingIcon = {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                    },
                )

                DropdownMenuItem(
                    text = { Text("Log Out") },
                    onClick = { showDialog = true },
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "log out icon button"
                        )
                    }
                )
                LogoutConfirmationDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onConfirm = { navViewModel.navigateToLogin() }
                )
                CustomBottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
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
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.SansSerif,
                    )
                }

            }
        },
        gesturesEnabled = true,
        drawerState = drawerState,
        content = content
    )
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
    content: @Composable (Int) -> Unit,
    optionsCount: Int,
    initialSelectedIndex: Int = 2,
    onIndexSelected: (Int) -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(initialSelectedIndex) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (index in 0 until optionsCount) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(height = 32.dp, width = 64.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        width = if (index == selectedIndex) 2.dp else 0.dp,
                        color = if (index == selectedIndex) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable {
                        selectedIndex = index
                        onIndexSelected(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                content(index)
            }
        }
    }
}

@Composable
fun SingleChoiceSegmentedButtonAlternativeExample(
    onClickNavigate: (Int) -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(2) }

    SingleChoiceSegmentedButtonAlternative(
        optionsCount = 5,
        initialSelectedIndex = 2,
        modifier = Modifier,
        onIndexSelected = { index ->
            selectedIndex = index
            onClickNavigate(index)
        },
        content = @Composable { index: Int ->
            when (index) {
                0 -> Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Fingerprint Icon",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = if (index == selectedIndex) 1f else 0.6f)
                )

                1 -> Icon(
                    painter = painterResource(Res.drawable.film),
                    contentDescription = "Fingerprint Icon",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = if (index == selectedIndex) 1f else 0.6f)
                )

                2 -> Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = if (index == selectedIndex) 1f else 0.6f)
                )

                3 -> Icon(
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = "List",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = if (index == selectedIndex) 1f else 0.6f)
                )

                4 -> Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Person",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = if (index == selectedIndex) 1f else 0.6f)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navigationIcon: (@Composable () -> Unit),
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color,
    contentColor: Color
) {
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
            navigationIconContentColor = contentColor
        )
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
                    text = { Text(text = item) },
                    onClick = {
                        selectedOption = item
                        expanded = false
                    }
                )
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
            style = MaterialTheme.typography.bodySmall,
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
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp),
                painter = painterResource(icon),
                contentDescription = description
            )
        }

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
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
            containerColor = Color.Red.copy(alpha = 0.7f),
            contentColor = Color.Black
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
            contentDescription = "log out icon button",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Log Out", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
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

@Composable
fun MovieItem(nav: NavigationController, movie: Movie) {
    AsyncImage(
        model = "https://image.tmdb.org/t/p/original${movie.posterPath}",
        contentDescription = "Movie Poster",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2 / 3f)
            .clip(MaterialTheme.shapes.small)
            .clickable { nav.navigateToDetail(movie) },
        placeholder = painterResource(Res.drawable.film)
    )
}


@Composable
fun MovieHeader(nav: NavigationController, movie: Movie) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .padding(vertical = 8.dp)
            .clickable { nav.navigateToDetail(movie) }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(18.dp))
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/original${movie.posterPath}",
                contentDescription = "Movie Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.film)
            )


        }
        Spacer(modifier = Modifier.height(8.dp))
        movie.title?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
movie.releaseDate?.let {
    val date = LocalDate.parse(it)
    val formattedDate = "${date.dayOfMonth} ${date.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }} ${date.year}"
    Text(
        text = formattedDate,
        fontSize = 14.sp,
        color = Color.Gray,
    )
}
    }
}

@Composable
fun ButtonRow(
    isSaved: MutableState<Boolean>,
    buttonColor: Color,
    movie: Movie,
    isFavorite: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Ocupa solo el espacio mínimo necesario
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top // Alinea los botones arriba
    ) {
        FloatingActionButton(
            onClick = { isSaved.value = !isSaved.value },
            modifier = Modifier
                .size(40.dp)
                .offset(y = (-20).dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(
                painter = if (isSaved.value) painterResource(Res.drawable.guardado) else painterResource(
                    Res.drawable.guardar
                ),
                contentDescription = "Add to Watchlist",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        FloatingActionButton(
            onClick = { /* TODO: Show average votes */ },
            modifier = Modifier
                .size(40.dp)
                .offset(y = (-20).dp),
            containerColor = buttonColor,
            contentColor = MaterialTheme.colorScheme.background,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Text(
                text = "${(movie.voteAverage.toDouble() * 10).toInt()}%",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        FloatingActionButton(
            onClick = { isFavorite.value = !isFavorite.value },
            modifier = Modifier
                .size(40.dp)
                .offset(y = (-20).dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(
                imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Add to Favorites",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun CircularAvgVotes(movie: Movie) {
    val votePercentage = (movie.voteAverage.toDouble() * 10).toInt()
    val borderColor = when {
        votePercentage < 40 -> Color.Red
        votePercentage < 70 -> Color.Yellow
        else -> Color(0xFF2AE98E)
    }
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color(0xFF18262B), CircleShape)
            .border(3.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$votePercentage%",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun MovieDetail(movie: Movie?) {

    if (movie != null) {
        var isFilled by remember { mutableStateOf(false) }

        Column {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original${movie.backdropPath}",
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(16 / 9f)
                )
            }

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
            CircularAvgVotes(movie)
//            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                item {
                    movie.title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    movie.releaseDate?.let {

                        val date: LocalDate = LocalDate.parse(it)
                        val formattedDate = "${
                            date.dayOfMonth.toString().padStart(2, '0')
                        }-${date.monthNumber.toString().padStart(2, '0')}-${date.year}"

                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    movie.overview?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(movie.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}