package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.ui.components.PlaceholderScreen
import org.lanzadera.proyectos.ui.components.sections.BookSection
import org.lanzadera.proyectos.ui.components.sections.FavoriteItemsGrid
import org.lanzadera.proyectos.ui.components.sections.GameSection
import org.lanzadera.proyectos.ui.components.sections.Section
import org.lanzadera.proyectos.ui.components.sections.TvShowSection
import org.lanzadera.proyectos.ui.components.tabs.NiaTab
import org.lanzadera.proyectos.ui.components.tabs.NiaTabRow
import org.lanzadera.proyectos.ui.models.BookUI
import org.lanzadera.proyectos.ui.models.MovieUI
import org.lanzadera.proyectos.ui.models.TvShowUI
import org.lanzadera.proyectos.ui.models.GameUI
import org.lanzadera.proyectos.ui.screens.home.tabs.FavoritesTabViewModel
import org.lanzadera.proyectos.ui.screens.home.tabs.BooksTabViewModel
import org.lanzadera.proyectos.ui.screens.home.tabs.FilmsTabViewModel
import org.lanzadera.proyectos.ui.screens.home.tabs.SeriesTabViewModel
import org.lanzadera.proyectos.ui.screens.home.tabs.GamesTabViewModel
import org.lanzadera.proyectos.utils.Constants.MenuOptions.topBarTitles
import org.lanzadera.proyectos.utils.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun HomeView(
    nav: NavHostController,
    vm: HomeViewModel = koinViewModel(),
    favoritesVM: FavoritesTabViewModel = koinViewModel(),
    booksVM: BooksTabViewModel = koinViewModel(),
    filmsVM: FilmsTabViewModel = koinViewModel(),
    seriesVM: SeriesTabViewModel = koinViewModel(),
    gamesVM: GamesTabViewModel = koinViewModel(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedTab by vm.selectedTab.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    org.lanzadera.proyectos.ui.components.DrawerAppBar(
        navViewModel = nav,
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
            topBar = {
                val selectedIndex = when (selectedTab) {
                    HomeViewModel.HomeTab.FAVORITES -> 0
                    HomeViewModel.HomeTab.BOOKS -> 1
                    HomeViewModel.HomeTab.FILMS -> 2
                    HomeViewModel.HomeTab.SERIES -> 3
                    HomeViewModel.HomeTab.GAMES -> 4
                }
                NiaTabRow(selectedTabIndex = selectedIndex) {
                    topBarTitles.forEachIndexed { index, title ->
                        NiaTab(
                            selected = selectedIndex == index,
                            onClick = { vm.selectTab(index) },
                            text = if (index == 0) {
                                // Primer tab: usar ícono
                                {
                                    Icon(
                                        imageVector = if (selectedIndex == 0) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Siguiendo",
                                        tint = if (selectedIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else {
                                // Resto de tabs: usar texto
                                {
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
            when (selectedTab) {
                HomeViewModel.HomeTab.FAVORITES -> {
                    FavoritesTabContent(
                        viewModel = favoritesVM,
                        nav = nav,
                        paddingValues = paddingValues
                    )
                }
                HomeViewModel.HomeTab.BOOKS -> {
                    BooksTabContent(
                        viewModel = booksVM,
                        nav = nav,
                        paddingValues = paddingValues
                    )
                }
                HomeViewModel.HomeTab.FILMS -> {
                    FilmsTabContent(
                        viewModel = filmsVM,
                        nav = nav,
                        paddingValues = paddingValues
                    )
                }
                HomeViewModel.HomeTab.SERIES -> {
                    SeriesTabContent(
                        viewModel = seriesVM,
                        nav = nav,
                        paddingValues = paddingValues
                    )
                }
                HomeViewModel.HomeTab.GAMES -> {
                    GamesTabContent(
                        viewModel = gamesVM,
                        nav = nav,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesTabContent(
    viewModel: FavoritesTabViewModel,
    nav: NavHostController,
    paddingValues: PaddingValues
) {
    val favoritesWithInfo by viewModel.favoritesWithInfo.collectAsStateWithLifecycle()

    if (favoritesWithInfo.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PlaceholderScreen(title = "No hay contenido por seguir")
        }
    } else {
        FavoriteItemsGrid(
            items = favoritesWithInfo,
            nav = nav,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun BooksTabContent(
    viewModel: BooksTabViewModel,
    nav: NavHostController,
    paddingValues: PaddingValues
) {
    val fictionBooks by viewModel.fictionBooks.collectAsStateWithLifecycle()
    val scienceBooks by viewModel.scienceBooks.collectAsStateWithLifecycle()
    val historyBooks by viewModel.historyBooks.collectAsStateWithLifecycle()
    val biographyBooks by viewModel.biographyBooks.collectAsStateWithLifecycle()
    val businessBooks by viewModel.businessBooks.collectAsStateWithLifecycle()
    val technologyBooks by viewModel.technologyBooks.collectAsStateWithLifecycle()
    val selfHelpBooks by viewModel.selfHelpBooks.collectAsStateWithLifecycle()
    val recentBooks by viewModel.recentBooks.collectAsStateWithLifecycle()
    val isRefreshingBooks by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val allBooksEmpty = fictionBooks.isEmpty() &&
            scienceBooks.isEmpty() && historyBooks.isEmpty() &&
            biographyBooks.isEmpty() && businessBooks.isEmpty() &&
            technologyBooks.isEmpty() && selfHelpBooks.isEmpty() &&
            recentBooks.isEmpty()

    if (allBooksEmpty) {
        if (isRefreshingBooks) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PlaceholderScreen(title = Strings.Placeholders.BOOKS)
        }
    } else {
        val booksListState = rememberLazyListState()

        LazyColumn(
            state = booksListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sections: List<Pair<String, List<BookUI>>> = listOf(
                "Novedades Destacadas" to recentBooks,
                "Ficción" to fictionBooks,
                "Ciencia" to scienceBooks,
                "Historia" to historyBooks,
                "Biografías" to biographyBooks,
                "Negocios" to businessBooks,
                "Tecnología" to technologyBooks,
                "Autoayuda" to selfHelpBooks,
            ).filter { it.second.isNotEmpty() }

            sections.forEachIndexed { idx, (sectionTitle, sectionBooks) ->
                item {
                    when (idx) {
                        0 -> BookSection(
                            sectionTitle,
                            sectionBooks,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.HEADER
                        )

                        1 -> BookSection(
                            sectionTitle,
                            sectionBooks,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_SHOW_META
                        )

                        else -> BookSection(
                            sectionTitle,
                            sectionBooks,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_HIDE_META
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesTabContent(
    viewModel: SeriesTabViewModel,
    nav: NavHostController,
    paddingValues: PaddingValues
) {
    val tvShows by viewModel.tvShows.collectAsStateWithLifecycle()
    val popularTvShows by viewModel.popularTvShows.collectAsStateWithLifecycle()
    val topRatedTvShows by viewModel.topRatedTvShows.collectAsStateWithLifecycle()
    val onAirTvShows by viewModel.onAirTvShows.collectAsStateWithLifecycle()
    val trendingTvShows by viewModel.trendingTvShows.collectAsStateWithLifecycle()
    val airingTodayTvShows by viewModel.airingTodayTvShows.collectAsStateWithLifecycle()
    val trendingTvShowsWeek by viewModel.trendingTvShowsWeek.collectAsStateWithLifecycle()
    val airingTodayAndTrendingTvShows by viewModel.airingTodayAndTrendingTvShows.collectAsStateWithLifecycle()
    val recommendedTvShows by viewModel.recommendedTvShows.collectAsStateWithLifecycle()
    val upcomingTvShows by viewModel.upcomingTvShows.collectAsStateWithLifecycle()
    val isRefreshingSeries by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (tvShows.isEmpty()) {
        if (isRefreshingSeries) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PlaceholderScreen(title = Strings.Menu.SEARCH)
        }
    } else {
        val seriesListState = rememberLazyListState()
        LazyColumn(
            state = seriesListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sections: List<Pair<String, List<TvShowUI>>> = listOf(
                Strings.TVShowSections.NOW_AIRING to onAirTvShows,
                Strings.TVShowSections.POPULAR_SERIES to popularTvShows,
                Strings.TVShowSections.TOP_RATED to topRatedTvShows,
                Strings.TVShowSections.TRENDING_TODAY to trendingTvShows,
                Strings.TVShowSections.AIRING_TODAY to airingTodayTvShows,
                Strings.TVShowSections.TRENDING_THIS_WEEK to trendingTvShowsWeek,
                Strings.TVShowSections.AIRING_TODAY_AND_TRENDING to airingTodayAndTrendingTvShows,
                Strings.TVShowSections.RECOMMENDED_FOR_YOU to recommendedTvShows,
                Strings.TVShowSections.COMING_SOON to upcomingTvShows,
                Strings.TVShowSections.NEW_AND_TRENDING to trendingTvShows.shuffled().take(20),
                Strings.TVShowSections.FAN_FAVORITES to topRatedTvShows.shuffled().take(15),
                Strings.TVShowSections.HIDDEN_GEMS to popularTvShows.filter {
                    (it.voteCount ?: 0) < 1000
                }.shuffled()
                    .take(15),
                Strings.TVShowSections.BINGE_WORTHY_PICKS to popularTvShows.shuffled().take(20),
                Strings.TVShowSections.CRITICS_CHOICE to topRatedTvShows.take(20),
                Strings.TVShowSections.NEXT_UP to tvShows.filter { it !in onAirTvShows }.shuffled()
                    .take(20),
                Strings.TVShowSections.POPULAR_CLASSICS to popularTvShows.take(20),
                Strings.TVShowSections.ALL_SERIES to tvShows,
            ).filter { it.second.isNotEmpty() }

            sections.forEachIndexed { idx, (sectionTitle, sectionTvShows) ->
                item {
                    when (idx) {
                        0 -> TvShowSection(
                            sectionTitle,
                            sectionTvShows,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.HEADER
                        )

                        1 -> TvShowSection(
                            sectionTitle,
                            sectionTvShows,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_SHOW_META
                        )


                        else -> TvShowSection(
                            sectionTitle,
                            sectionTvShows,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_HIDE_META
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GamesTabContent(
    viewModel: GamesTabViewModel,
    nav: NavHostController,
    paddingValues: PaddingValues
) {
    val games by viewModel.games.collectAsStateWithLifecycle()
    val popularGames by viewModel.popularGames.collectAsStateWithLifecycle()
    val topRatedGames by viewModel.topRatedGames.collectAsStateWithLifecycle()
    val upcomingGames by viewModel.upcomingGames.collectAsStateWithLifecycle()
    val trendingGames by viewModel.trendingGames.collectAsStateWithLifecycle()

    val allGamesEmpty = games.isEmpty() && popularGames.isEmpty() &&
            topRatedGames.isEmpty() && upcomingGames.isEmpty() && trendingGames.isEmpty()

    if (allGamesEmpty) {
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val gamesListState = rememberLazyListState()
        LazyColumn(
            state = gamesListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sections: List<Pair<String, List<GameUI>>> =
                listOf(
                    "Juegos Populares" to popularGames,
                    "Top Rated" to topRatedGames,
                    "Próximos Estrenos" to upcomingGames,
                    "Tendencias Actuales" to trendingGames,
                    "Todos los Juegos" to games,
                ).filter { it.second.isNotEmpty() }

            sections.forEachIndexed { idx, (sectionTitle, sectionGames) ->
                item {
                    when (idx) {
                        0 -> GameSection(
                            sectionTitle,
                            sectionGames,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.HEADER
                        )

                        1 -> GameSection(
                            sectionTitle,
                            sectionGames,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_SHOW_META
                        )

                        else -> GameSection(
                            sectionTitle,
                            sectionGames,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_HIDE_META
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilmsTabContent(
    viewModel: FilmsTabViewModel,
    nav: NavHostController,
    paddingValues: PaddingValues
) {
    val popular by viewModel.popular.collectAsStateWithLifecycle()
    val topRated by viewModel.topRated.collectAsStateWithLifecycle()
    val trendingWeek by viewModel.trendingWeek.collectAsStateWithLifecycle()
    val trendingDay by viewModel.trendingDay.collectAsStateWithLifecycle()
    val upcoming by viewModel.upcoming.collectAsStateWithLifecycle()
    val discover by viewModel.discover.collectAsStateWithLifecycle()
    val hero by viewModel.hero.collectAsStateWithLifecycle()
    val inCinemasToday by viewModel.inCinemasToday.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 8.dp,
            bottom = 80.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val sections: List<Pair<String, List<MovieUI>>> = listOf(
            Strings.MovieSections.UPCOMING to upcoming,
            Strings.MovieSections.POPULAR_MOVIES to popular,
            Strings.MovieSections.DISCOVER to discover,
            Strings.MovieSections.IN_CINEMAS_TODAY to inCinemasToday,
            Strings.MovieSections.TOP_RATED to topRated,
            Strings.MovieSections.TRENDING_TODAY to trendingDay,
            Strings.MovieSections.TRENDING_THIS_WEEK to trendingWeek,
            Strings.MovieSections.HERO_PICKS to hero,
        )

        sections.forEachIndexed { idx, (sectionTitle, items) ->
            if (items.isNotEmpty()) {
                item {
                    when (idx) {
                        0 -> Section(
                            sectionTitle,
                            items,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.HEADER
                        )

                        1 -> Section(
                            sectionTitle,
                            items,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_SHOW_META
                        )

                        else -> Section(
                            sectionTitle,
                            items,
                            nav,
                            sectionIndex = idx,
                            mode = SectionMode.SUBHEADER_HIDE_META
                        )
                    }
                }
            }
        }
    }
}
