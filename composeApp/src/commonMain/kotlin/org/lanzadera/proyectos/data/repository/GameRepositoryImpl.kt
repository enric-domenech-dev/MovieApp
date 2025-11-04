package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.BuildConfig
import org.lanzadera.proyectos.data.authentication.IGDBAuthManager
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.repository.GameRepository

class GameRepositoryImpl(
    private val authManager: IGDBAuthManager,
    private val client: HttpClient,
    private val maxPages: Int,
    private val json: Json
) : GameRepository {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    override val gamesFlow: StateFlow<List<Game>> = _games

    private val _popularGames = MutableStateFlow<List<Game>>(emptyList())
    override val popularGamesFlow: StateFlow<List<Game>> = _popularGames

    private val _topRatedGames = MutableStateFlow<List<Game>>(emptyList())
    override val topRatedGamesFlow: StateFlow<List<Game>> = _topRatedGames

    private val _upcomingGames = MutableStateFlow<List<Game>>(emptyList())
    override val upcomingGamesFlow: StateFlow<List<Game>> = _upcomingGames

    private val _trendingGames = MutableStateFlow<List<Game>>(emptyList())
    override val trendingGamesFlow: StateFlow<List<Game>> = _trendingGames

    private val lastUpdated = mutableMapOf<MutableStateFlow<List<Game>>, Long>()
    private val TTL = 2 * 60 * 1000L // 2 min

    private val gameDetailsCache = mutableMapOf<Int, Game>()

    private fun isValidGame(game: Game): Boolean =
        game.id != null && game.name != null && game.cover != null

    private suspend inline fun refreshFeed(
        state: MutableStateFlow<List<Game>>,
        query: String
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        val last = lastUpdated[state] ?: 0L

        if ((now - last) < TTL && state.value.isNotEmpty()) {
            return
        }

        try {
            val accessToken = authManager.getAccessToken()
            val clientId = BuildConfig.IGDB_CLIENT_ID

            println("SYNCRO: Sending request with Authorization: Bearer ${accessToken.take(20)}...")

            val response = client.post("https://api.igdb.com/v4/games") {
                headers {
                    append("Client-ID", clientId)
                    append("Authorization", "Bearer $accessToken")
                }
                contentType(ContentType.Text.Plain)
                setBody(query)
            }

            println("SYNCRO: Response status: ${response.status}")

            val games = json.decodeFromString<List<Game>>(response.bodyAsText())
            val validGames = games.filter { isValidGame(it) }

            state.value = validGames
            lastUpdated[state] = now
        } catch (e: Exception) {
            println("SYNCRO GameRepository: Error - ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun refreshGames(page: Int) {
        val query =
            "fields name, summary, cover.image_id, rating, genres.name, platforms.name; sort popularity desc; limit 20; offset ${(page - 1) * 20};"
        refreshFeed(_games, query)
    }

    override suspend fun refreshPopularGames(page: Int) {
        val query =
            "fields name, summary, cover.image_id, rating, genres.name, platforms.name; sort popularity desc; limit 20; offset ${(page - 1) * 20};"
        refreshFeed(_popularGames, query)
    }

    override suspend fun refreshTopRatedGames(page: Int) {
        val query =
            "fields name, summary, cover.image_id, rating, genres.name, platforms.name; where rating != null; sort rating desc; limit 20; offset ${(page - 1) * 20};"
        refreshFeed(_topRatedGames, query)
    }

    override suspend fun refreshUpcomingGames(page: Int) {
        val now = Clock.System.now().toEpochMilliseconds() / 1000
        val query =
            "fields name, summary, cover.image_id, rating, genres.name, platforms.name; where release_dates.date > $now; sort release_dates.date asc; limit 20; offset ${(page - 1) * 20};"
        refreshFeed(_upcomingGames, query)
    }

    override suspend fun refreshTrendingGames(page: Int) {
        val query =
            "fields name, summary, cover.image_id, rating, genres.name, platforms.name; where hype > 0; sort hype desc; limit 20; offset ${(page - 1) * 20};"
        refreshFeed(_trendingGames, query)
    }

    override suspend fun getGameDetails(gameId: Int): Game? {
        gameDetailsCache[gameId]?.let { return it }

        return try {
            val accessToken = authManager.getAccessToken()
            val query =
                "fields name, summary, cover.image_id, rating, genres.name, platforms.name, screenshots.image_id; where id = $gameId;"

            val response = client.post("https://api.igdb.com/v4/games") {
                headers {
                    append("Client-ID", BuildConfig.IGDB_CLIENT_ID)
                    append("Authorization", "Bearer $accessToken")
                }
                contentType(ContentType.Text.Plain)
                setBody(query)
            }

            val games = json.decodeFromString<List<Game>>(response.bodyAsText())
            val game = games.firstOrNull()

            if (game != null) {
                gameDetailsCache[gameId] = game
            }

            game
        } catch (e: Exception) {
            null
        }
    }
}

