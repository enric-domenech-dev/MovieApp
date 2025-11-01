package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.film
import org.jetbrains.compose.resources.painterResource


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
