package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.MovieItem
import org.lanzadera.proyectos.ui.components.MovieSubheader
import org.lanzadera.proyectos.ui.components.BookItem
import org.lanzadera.proyectos.ui.components.tabs.NiaTab
import org.lanzadera.proyectos.ui.components.tabs.NiaTabRow
import org.lanzadera.proyectos.utils.Constants.MenuOptions.topBarTitles

@Composable
@Preview
fun HomeView(
    nav: NavHostController,
    vm: HomeViewModel,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedTab by vm.selectedTab.collectAsStateWithLifecycle()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val primary by vm.primary.collectAsStateWithLifecycle()
    val secondary by vm.secondary.collectAsStateWithLifecycle()
    val popular by vm.popular.collectAsStateWithLifecycle()
    val topRated by vm.topRated.collectAsStateWithLifecycle()
    val trendingWeek by vm.trendingWeek.collectAsStateWithLifecycle()
    val trendingDay by vm.trendingDay.collectAsStateWithLifecycle()
    val upcoming by vm.upcoming.collectAsStateWithLifecycle()
    val discover by vm.discover.collectAsStateWithLifecycle()
    val hero by vm.hero.collectAsStateWithLifecycle()
    val inCinemasToday by vm.inCinemasToday.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Derived ordering example
    val primarySorted by remember(primary) {
        derivedStateOf<List<Movie>> {
            primary.sortedWith(
                compareByDescending<Movie> { it.releaseDate }
                    .thenByDescending { it.voteCount }
            ).take(35)
        }
    }

    org.lanzadera.proyectos.ui.components.DrawerAppBar(
        modifier = Modifier.safeDrawingPadding(),
        navViewModel = nav,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                val selectedIndex = when (selectedTab) {
                    HomeViewModel.HomeTab.BOOKS -> 0
                    HomeViewModel.HomeTab.FILMS -> 1
                    HomeViewModel.HomeTab.SERIES -> 2
                    HomeViewModel.HomeTab.GAMES -> 3
                    HomeViewModel.HomeTab.HEART -> 4
                }
                NiaTabRow(selectedTabIndex = selectedIndex) {
                    topBarTitles.forEachIndexed { index, title ->
                        NiaTab(
                            selected = selectedIndex == index,
                            onClick = { vm.selectTab(index) },
                            text = {
                                if (index == HomeViewModel.HomeTab.HEART.ordinal) {
                                    // Heart tab: show filled when selected, outlined when not
                                    Icon(
                                        imageVector = if (selectedIndex == index) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favoritos",
                                        tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        text = title.uppercase(),
                                        style = TextStyle(
                                            fontSize = 11.sp,
                                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            fontFamily = FontFamily.SansSerif,
                                            color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    )
                                }
                            },
                        )
                    }
                }
            },
            // bottomBar moved to top-level Navigation scaffold
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val hasContent = primarySorted.isNotEmpty() || secondary.isNotEmpty()
                val showLoading = state.isLoading && !hasContent
                val showError = (state.error != null) && !hasContent

                when {
                    showLoading -> CircularProgressIndicator()
                    showError -> {
                        Text(text = "Error", color = MaterialTheme.colorScheme.error)
                        Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error)
                    }
                    else -> {
                        // Home screen content (tabs + sections)
                        if (selectedTab == HomeViewModel.HomeTab.BOOKS) {
                            // Mostrar lista de libros usando los nuevos composables
                            val books by vm.books.collectAsStateWithLifecycle()
                            val isRefreshingBooks by vm.refreshing.collectAsStateWithLifecycle()

                            if (books.isEmpty()) {
                                // Si no hay libros, mostrar placeholder o loading según el flag específico de books
                                if (isRefreshingBooks) {
                                    CircularProgressIndicator()
                                } else {
                                    org.lanzadera.proyectos.ui.components.PlaceholderScreen(title = "BOOKS")
                                }
                            } else {
                                 // Grid de libros similar a la dialog de sección
                                 val gridState = rememberLazyGridState()
                                 LazyVerticalGrid(
                                     columns = GridCells.Fixed(3),
                                     state = gridState,
                                     contentPadding = PaddingValues(8.dp),
                                     verticalArrangement = Arrangement.spacedBy(8.dp),
                                     horizontalArrangement = Arrangement.spacedBy(8.dp),
                                     modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
                                 ) {
                                     itemsIndexed(books, key = { _, book -> book.id ?: book.hashCode().toString() }) { _, book ->
                                         org.lanzadera.proyectos.ui.components.BookItem(nav, book)
                                     }
                                 }
                             }

                        } else if (selectedTab != HomeViewModel.HomeTab.FILMS) {
                            org.lanzadera.proyectos.ui.components.PlaceholderScreen(
                                 title = when (selectedTab) {
                                     HomeViewModel.HomeTab.BOOKS -> "BOOKS"
                                     HomeViewModel.HomeTab.SERIES -> "SERIES"
                                     HomeViewModel.HomeTab.GAMES -> "GAMES"
                                     HomeViewModel.HomeTab.HEART -> "FAVORITOS"
                                     else -> ""
                                 }
                            )
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val sections: List<Pair<String, List<Movie>>> = listOf(
                                    "Upcoming" to upcoming,
                                    "Popular Movies" to popular,
                                    "Discover" to discover,
                                    "In Cinemas Today" to inCinemasToday,
                                    "Top Rated" to topRated,
                                    "Trending Today" to trendingDay,
                                    "Trending This Week" to trendingWeek,
                                    "Hero Picks" to hero,
                                )

                                sections.forEachIndexed { idx, (sectionTitle, items) ->
                                    if (items.isNotEmpty()) {
                                        item {
                                            when (idx) {
                                                0 -> Section(sectionTitle, items, nav, sectionIndex = idx, mode = SectionMode.HEADER)
                                                1 -> Section(sectionTitle, items, nav, sectionIndex = idx, mode = SectionMode.SUBHEADER_SHOW_META)
                                                else -> Section(sectionTitle, items, nav, sectionIndex = idx, mode = SectionMode.SUBHEADER_HIDE_META)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class SectionMode { HEADER, SUBHEADER_SHOW_META, SUBHEADER_HIDE_META }

@Composable
private fun Section(title: String, items: List<Movie>, nav: NavHostController, sectionIndex: Int = 0, mode: SectionMode = SectionMode.HEADER) {
    val visibleItems = remember(items) { items.take(12) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogContentVisible by remember { mutableStateOf(false) }
    val animDuration = 320 // ms

    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "VER MÁS... (${items.size})",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable {
                    showDialog = true
                    dialogContentVisible = true
                }
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val spacing = 16.dp
            val horizontalPadding = 16.dp
            val available = maxWidth - horizontalPadding
            val baseWidth = (available - spacing) / 2f
            val headerWidth = baseWidth
            val subWidth = baseWidth * 0.65f
            val rowContentPadding = PaddingValues(horizontal = 8.dp)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.Top,
                contentPadding = rowContentPadding,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                itemsIndexed(visibleItems, key = { index, movie ->
                    // unique key combining sectionIndex, movie id/hash and index
                    val idPart = movie.id?.toString() ?: movie.hashCode().toString()
                    "s${sectionIndex}_${idPart}_$index"
                }) { _, movie ->
                     val itemModifier = if (mode == SectionMode.HEADER) Modifier.width(headerWidth) else Modifier.width(subWidth)
                     when (mode) {
                         SectionMode.HEADER -> org.lanzadera.proyectos.ui.components.MovieHeader(modifier = itemModifier, nav = nav, movie = movie)
                         SectionMode.SUBHEADER_SHOW_META -> org.lanzadera.proyectos.ui.components.MovieSubheader(modifier = itemModifier, nav = nav, movie = movie, showMeta = true)
                         SectionMode.SUBHEADER_HIDE_META -> org.lanzadera.proyectos.ui.components.MovieSubheader(modifier = itemModifier, nav = nav, movie = movie, showMeta = false)
                     }
                 }
             }

            if (showDialog) {
                SectionDialog(
                    title = title,
                    items = items,
                    nav = nav,
                    sectionIndex = sectionIndex,
                    dialogVisible = dialogContentVisible,
                    onRequestHideContent = { dialogContentVisible = false },
                    onDismissed = { showDialog = false },
                    animDuration = animDuration
                )
            }
         }
     }
}

@Composable
private fun SectionDialog(
    title: String,
    items: List<Movie>,
    nav: NavHostController,
    sectionIndex: Int,
    dialogVisible: Boolean,
    onRequestHideContent: () -> Unit,
    onDismissed: () -> Unit,
    animDuration: Int
) {
    Dialog(onDismissRequest = { onRequestHideContent() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        AnimatedVisibility(
            visible = dialogVisible,
            enter = fadeIn(animationSpec = tween(animDuration)) + slideInVertically(animationSpec = tween(animDuration)) { it / 4 },
            exit = fadeOut(animationSpec = tween(animDuration)) + slideOutVertically(animationSpec = tween(animDuration)) { it / 4 }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = title, style = MaterialTheme.typography.headlineSmall)
                        IconButton(onClick = { onRequestHideContent() }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Cerrar")
                        }
                    }

                    val gridState = rememberLazyGridState()
                    LazyVerticalGrid(
                         columns = GridCells.Fixed(3),
                         state = gridState,
                         contentPadding = PaddingValues(8.dp),
                         verticalArrangement = Arrangement.spacedBy(8.dp),
                         horizontalArrangement = Arrangement.spacedBy(8.dp),
                         modifier = Modifier.fillMaxSize()
                     ) {
                        itemsIndexed(items, key = { index, movie ->
                            val idPart = movie.id?.toString() ?: movie.hashCode().toString()
                            // include sectionIndex to avoid collisions across sections
                            "s${sectionIndex}_${idPart}_$index"
                        }) { _, movie ->
                            org.lanzadera.proyectos.ui.components.MovieItem(nav, movie)
                        }
                       }
                 }
             }
         }
     }

    if (!dialogVisible) {
        LaunchedEffect(dialogVisible) {
            delay(animDuration.toLong())
            onDismissed()
        }
    }
}

