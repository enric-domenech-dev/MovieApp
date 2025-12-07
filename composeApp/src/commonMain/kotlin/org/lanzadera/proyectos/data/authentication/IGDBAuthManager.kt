package org.lanzadera.proyectos.data.authentication

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.utils.Logger

@Serializable
data class IGDBAuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("token_type")
    val tokenType: String
)

/**
 * Manages IGDB OAuth2 authentication.
 * Handles token refresh automatically when expired.
 */
class IGDBAuthManager(
    private val clientId: String,
    private val clientSecret: String,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val mutex = Mutex()
    private var currentToken: IGDBAuthToken? = null
    private var tokenExpirationTime: Long = 0

    /**
     * Gets a valid access token, refreshing if necessary.
     */
    suspend fun getAccessToken(): String {
        mutex.withLock {
            val now = Clock.System.now().toEpochMilliseconds()

            // Check if token exists and is still valid (with 60 second buffer)
            if (currentToken != null && tokenExpirationTime > now + 60000) {
                return currentToken!!.accessToken
            }

            // Refresh token
            refreshToken()
            return currentToken!!.accessToken
        }
    }

    /**
     * Refreshes the access token from IGDB OAuth endpoint.
     */
    private suspend fun refreshToken() {
        try {
            val url = "https://id.twitch.tv/oauth2/token?" +
                    "client_id=$clientId&" +
                    "client_secret=$clientSecret&" +
                    "grant_type=client_credentials"

            val response = httpClient.post(url)

            val responseBody = response.bodyAsText()
            Logger.d("$responseBody", tag = "IGDBAuthManager")

            currentToken = json.decodeFromString<IGDBAuthToken>(responseBody)
            tokenExpirationTime = Clock.System.now().toEpochMilliseconds() +
                    (currentToken!!.expiresIn * 1000L)

            Logger.d("IGDB Token refreshed, expires in ${currentToken!!.expiresIn} seconds", tag = "IGDBAuthManager")
        } catch (e: Exception) {
            Logger.d("${e.message}", tag = "IGDBAuthManager")
            e.printStackTrace()
            throw RuntimeException("Failed to authenticate with IGDB", e)
        }
    }
}

