package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.apiKey
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DrawerAppBar



@Composable
@Preview
fun HomeView(
    navigation: NavigationController, vm: HomeViewModel
) {
    val number = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("Loading") }

    LaunchedEffect(true) {
        scope.launch {
            text = try {
                val apiKey = Res.string.apiKey
                vm.data("$apiKey")
            } catch (e: Exception) {
                e.message ?: "error"
            }
        }
    }


    DrawerAppBar(
        navViewModel = navigation,
        pageTitle = "HOME",
        actionButton = {
            FloatingActionButton(
                onClick = {
                    number.value++
                },
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        screenContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Home Screen")
                Spacer(modifier = Modifier.height(8.dp))
                Text("${number.value}")
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    item {
                        Text(text)
                    }
                }
            }
        }
    )

}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}
