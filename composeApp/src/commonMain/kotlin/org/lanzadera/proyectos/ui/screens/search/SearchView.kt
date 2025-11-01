package org.lanzadera.proyectos.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import androidx.navigation.NavHostController
import org.lanzadera.proyectos.ui.components.navComponents.NiaNavigationBar
import org.lanzadera.proyectos.ui.components.navComponents.NiaNavigationBarItem
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarIcons
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarSelectedIcons
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarTitles

@Composable
@Preview
fun SearchView(
    navIndexBottomBar: Int = 1,
    nav: NavHostController, vm: SearchViewModel,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {

        },
        // bottomBar moved to top-level Navigation scaffold
    ) { paddingValues ->

        var text by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.extraLarge,
                singleLine = true,
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,

                ),
                leadingIcon = {
                    IconButton(
                        onClick = {
                            // enviar busqueda
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    }
                },
                isError = false,
                enabled = true,
                readOnly = false,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // limpiar texto
                            text = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Icon",
                            tint = Color.Gray
                        )
                    }
                },
            )
        }
    }
}