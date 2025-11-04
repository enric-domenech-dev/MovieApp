package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.ui.components.PlaceholderScreen
import org.lanzadera.proyectos.ui.components.sections.BookSection
import org.lanzadera.proyectos.ui.components.sections.GameSection
import org.lanzadera.proyectos.ui.components.sections.Section
import org.lanzadera.proyectos.ui.components.sections.TvShowSection
import org.lanzadera.proyectos.ui.components.tabs.NiaTab
import org.lanzadera.proyectos.ui.components.tabs.NiaTabRow
import org.lanzadera.proyectos.utils.Constants.MenuOptions.topBarTitles
import org.lanzadera.proyectos.utils.Strings

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
        derivedStateOf {
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
            val hasContent = primarySorted.isNotEmpty() || secondary.isNotEmpty()
            val showLoading = state.isLoading && !hasContent
            val showError = (state.error != null) && !hasContent

            when {
                showLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                showError -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = Strings.Generic.ERROR, color = MaterialTheme.colorScheme.error)
                        Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error)
                    }
                }

                selectedTab == HomeViewModel.HomeTab.BOOKS -> {
                    // Mostrar lista de libros con secciones
                    val books by vm.books.collectAsStateWithLifecycle()
                    val isRefreshingBooks by vm.refreshing.collectAsStateWithLifecycle()

                    if (books.isEmpty()) {
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
                            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = paddingValues.calculateTopPadding() + 8.dp,
                                bottom = paddingValues.calculateBottomPadding() +
                                        WindowInsets.navigationBars.asPaddingValues()
                                            .calculateBottomPadding()
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val sections: List<Pair<String, List<Book>>> = listOf(
                                Strings.BookSections.FEATURED_BOOKS to books.take(35),
                                Strings.BookSections.POPULAR_BOOKS to books.drop(35).take(35),
                                Strings.BookSections.LATEST_RELEASES to books.drop(70).take(35),
                                Strings.BookSections.TRENDING_BOOKS to books.drop(105).take(35),
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

                selectedTab == HomeViewModel.HomeTab.SERIES -> {
                    // Mostrar lista de series con secciones
                    val tvShows by vm.tvShows.collectAsStateWithLifecycle()
                    val popularTvShows by vm.popularTvShows.collectAsStateWithLifecycle()
                    val topRatedTvShows by vm.topRatedTvShows.collectAsStateWithLifecycle()
                    val onAirTvShows by vm.onAirTvShows.collectAsStateWithLifecycle()
                    val trendingTvShows by vm.trendingTvShows.collectAsStateWithLifecycle()
                    val airingTodayTvShows by vm.airingTodayTvShows.collectAsStateWithLifecycle()
                    val trendingTvShowsWeek by vm.trendingTvShowsWeek.collectAsStateWithLifecycle()
                    val airingTodayAndTrendingTvShows by vm.airingTodayAndTrendingTvShows.collectAsStateWithLifecycle()
                    val recommendedTvShows by vm.recommendedTvShows.collectAsStateWithLifecycle()
                    val upcomingTvShows by vm.upcomingTvShows.collectAsStateWithLifecycle()
                    val isRefreshingSeries by vm.refreshing.collectAsStateWithLifecycle()

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
                            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = paddingValues.calculateTopPadding() + 8.dp,
                                bottom = paddingValues.calculateBottomPadding() +
                                        WindowInsets.navigationBars.asPaddingValues()
                                            .calculateBottomPadding()
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val sections: List<Pair<String, List<TvShow>>> = listOf(
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

                selectedTab == HomeViewModel.HomeTab.GAMES -> {
                    // Mostrar lista de juegos con secciones
                    val games by vm.games.collectAsStateWithLifecycle()
                    val popularGames by vm.popularGames.collectAsStateWithLifecycle()
                    val topRatedGames by vm.topRatedGames.collectAsStateWithLifecycle()
                    val upcomingGames by vm.upcomingGames.collectAsStateWithLifecycle()
                    val trendingGames by vm.trendingGames.collectAsStateWithLifecycle()

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
                            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = paddingValues.calculateTopPadding() + 8.dp,
                                bottom = paddingValues.calculateBottomPadding() +
                                        WindowInsets.navigationBars.asPaddingValues()
                                            .calculateBottomPadding()
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val sections: List<Pair<String, List<org.lanzadera.proyectos.domain.models.game.Game>>> =
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

                selectedTab == HomeViewModel.HomeTab.HEART -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PlaceholderScreen(title = Strings.Placeholders.FAVORITES)
                    }
                }

                else -> {
                    // FILMS tab
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            end = 8.dp,
                            top = paddingValues.calculateTopPadding() + 8.dp,
                            bottom = paddingValues.calculateBottomPadding() + 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val sections: List<Pair<String, List<Movie>>> = listOf(
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
            }
        }
    }
}