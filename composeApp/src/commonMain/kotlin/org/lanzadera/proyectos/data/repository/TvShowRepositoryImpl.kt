package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.tvshow.SeasonDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowResponseDto
import org.lanzadera.proyectos.data.mapper.toDomain
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.utils.Constants
import org.lanzadera.proyectos.utils.Logger

class TvShowRepositoryImpl(
    private val client: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : TvShowRepository {

    private val _tvShows = MutableStateFlow<List<TvShow>>(emptyList())
    override val tvShowsFlow: StateFlow<List<TvShow>> = _tvShows

    private val _popularTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    override val popularTvShowsFlow: StateFlow<List<TvShow>> = _popularTvShows

    private val _topRatedTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    override val topRatedTvShowsFlow: StateFlow<List<TvShow>> = _topRatedTvShows

    private val _onAirTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    override val onAirTvShowsFlow: StateFlow<List<TvShow>> = _onAirTvShows

    private val _trendingTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    override val trendingTvShowsFlow: StateFlow<List<TvShow>> = _trendingTvShows

    private val _airingTodayTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    override val airingTodayTvShowsFlow: StateFlow<List<TvShow>> = _airingTodayTvShows

    private val _trendingTvShowsWeek = MutableStateFlow<List<TvShow>>(emptyList())
    override val trendingTvShowsWeekFlow: StateFlow<List<TvShow>> = _trendingTvShowsWeek

    private val lastUpdated = mutableMapOf<MutableStateFlow<List<TvShow>>, Long>()
    private val ttl = Constants.Cache.DEFAULT_TTL_MS

    private val tvShowDetailsCache = mutableMapOf<Int, TvShow>()

    private fun isValidTvShow(tvShow: TvShow): Boolean =
        tvShow.id != null &&
                tvShow.name != null &&
                tvShow.posterPath != null &&
                tvShow.overview != null &&
                tvShow.firstAirDate != null &&
                tvShow.voteCount != null &&
                tvShow.popularity != null &&
                tvShow.originalLanguage != null &&
                tvShow.backdropPath != null

    private suspend inline fun refreshFeed(
        state: MutableStateFlow<List<TvShow>>,
        force: Boolean,
        ttlMillis: Long = 0L,
        lastUpdated: MutableMap<MutableStateFlow<List<TvShow>>, Long>,
        crossinline fetch: suspend () -> List<TvShow>
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        val last = lastUpdated[state] ?: 0L
        val freshEnough = ttlMillis > 0 && (now - last) < ttlMillis

        Logger.d("force=$force, state.size=${state.value.size}, freshEnough=$freshEnough")
        if (!force && (state.value.isNotEmpty() || freshEnough)) {
            Logger.d("skipping fetch, cache is fresh")
            return
        }
        Logger.d("fetching new data")
        val data = fetch()
        state.value = data
        lastUpdated[state] = now
        Logger.d("updated state with ${data.size} tv shows")
    }

    override suspend fun refreshTvShows(force: Boolean) =
        refreshFeed(_tvShows, force, ttl, lastUpdated) { fetchTrendingTvShowsWeek() }

    override suspend fun refreshPopularTvShows(force: Boolean) =
        refreshFeed(_popularTvShows, force, ttl, lastUpdated) { fetchPopularTvShows() }

    override suspend fun refreshTopRatedTvShows(force: Boolean) =
        refreshFeed(_topRatedTvShows, force, ttl, lastUpdated) { fetchTopRatedTvShows() }

    override suspend fun refreshOnAirTvShows(force: Boolean) =
        refreshFeed(_onAirTvShows, force, ttl, lastUpdated) { fetchOnAirTvShows() }

    override suspend fun refreshTrendingTvShows(force: Boolean) =
        refreshFeed(_trendingTvShows, force, ttl, lastUpdated) { fetchTrendingTvShowsDay() }

    override suspend fun refreshAiringTodayTvShows(force: Boolean) =
        refreshFeed(_airingTodayTvShows, force, ttl, lastUpdated) { fetchAiringTodayTvShows() }

    override suspend fun refreshTrendingTvShowsWeek(force: Boolean) =
        refreshFeed(_trendingTvShowsWeek, force, ttl, lastUpdated) { fetchTrendingTvShowsWeek() }

    override suspend fun getTvShowDetails(tvShowId: Int): TvShow? {
        // Intenta cache primero
        if (tvShowDetailsCache.containsKey(tvShowId)) {
            return tvShowDetailsCache[tvShowId]
        }

        return try {
            val text = client.get("/3/tv/$tvShowId") {
                url {
                    parameters.append("append_to_response", "seasons,aggregate_credits")
                    parameters.append("language", "es")
                }
            }.bodyAsText()
            val tvShowDto: TvShowDto = json.decodeFromString(text)
            var tvShow: TvShow = tvShowDto.toDomain()

            // Cargar episodios para cada temporada
            tvShow = tvShow.copy(
                seasons = tvShow.seasons?.mapNotNull { season ->
                    try {
                        val seasonText = client.get("/3/tv/$tvShowId/season/${season.seasonNumber}") {
                            url {
                                parameters.append("language", "es")
                            }
                        }.bodyAsText()
                        val seasonDto: SeasonDto = json.decodeFromString(seasonText)
                        seasonDto.toDomain()
                    } catch (e: Exception) {
                        Logger.d("error fetching season ${season.seasonNumber}: ${e.message}")
                        season // Devuelve la temporada sin episodios si falla
                    }
                }
            )

            tvShowDetailsCache[tvShowId] = tvShow
            tvShow
        } catch (t: Throwable) {
            Logger.d("error fetching tv show details: ${t.message}")
            null
        }
    }

    private suspend fun fetchTrendingTvShowsWeek(): List<TvShow> =
        fetchPaged("/3/trending/tv/week", mapOf("language" to "es"))

    private suspend fun fetchTrendingTvShowsDay(): List<TvShow> =
        fetchPaged("/3/trending/tv/day", mapOf("language" to "es"))

    private suspend fun fetchPopularTvShows(): List<TvShow> =
        fetchPaged(
            path = "/3/tv/popular",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchTopRatedTvShows(): List<TvShow> =
        fetchPaged("/3/tv/top_rated", mapOf("language" to "es"))

    private suspend fun fetchOnAirTvShows(): List<TvShow> =
        fetchPaged(
            path = "/3/tv/on_the_air",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchAiringTodayTvShows(): List<TvShow> =
        fetchPaged(
            path = "/3/tv/airing_today",
            baseParams = mapOf("language" to "es")
        )

    private suspend fun fetchPaged(path: String, baseParams: Map<String, String>): List<TvShow> {
        val acc = mutableListOf<TvShow>()
        for (page in 1..maxPages) {
            val text = client.get(path) {
                url {
                    baseParams.forEach { (k, v) -> parameters.append(k, v) }
                    parameters.append("page", page.toString())
                }
            }.bodyAsText()

            val dto: TvShowResponseDto = json.decodeFromString(text)
            val valid = dto.results.map { it.toDomain() }.filter(::isValidTvShow)

            if (valid.isEmpty()) break
            acc += valid
            Logger.d("page $page, downloaded ${valid.size} tv shows, total so far: ${acc.size}")
        }
        Logger.d("finished, total tv shows downloaded: ${acc.size}")
        return acc
    }
}

