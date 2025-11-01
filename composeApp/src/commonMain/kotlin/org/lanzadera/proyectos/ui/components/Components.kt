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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.navigation.NavigationStore
import org.lanzadera.proyectos.utils.Constants


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
                    contentDescription = Icons.Outlined.Search.name,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = fl(index, selectedIndex))
                )

                1 -> Icon(
                    painter = painterResource(Res.drawable.film),
                    contentDescription = "Fingerprint Icon",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = fl(index, selectedIndex))
                )

                2 -> Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = fl(index, selectedIndex))
                )

                3 -> Icon(
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = "List",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = fl(index, selectedIndex))
                )

                4 -> Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Person",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = fl(index, selectedIndex))
                )
            }
        }
    )
}

private fun fl(index: Int, selectedIndex: Int) =
    if (index == selectedIndex) 1f else 0.6f

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

// Components that were moved to dedicated files for clarity:
// - Inputs (EmailInput, PasswordInput, PrimaryButton, FingerPrintAuthentication, DevelopingDialog)
//   -> see: ui.components.InputsComponents.kt
// - Movie components (MovieItem, MovieHeader, MovieSubheader, MovieDetail, ButtonRow, CircularAvgVotes)
//   -> see: ui.components.MovieComponents.kt
// The implementations were intentionally removed from this file to avoid duplicate symbols.
