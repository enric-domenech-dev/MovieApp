package org.lanzadera.proyectos.ui.models

/**
 * UI model for Game.
 * 
 * Simplified version of domain Game with only fields needed for UI display.
 */
data class GameUI(
    val id: Int,
    val name: String,
    val summary: String?,
    val coverUrl: String?,
    val releaseDate: Long?,
    val rating: Double?,
    val ratingCount: Int?,
    val genres: List<String>?,
    val platforms: List<String>?
) {
    val coverImageUrl: String
        get() = coverUrl ?: ""
    
    val hasValidCover: Boolean
        get() = !coverUrl.isNullOrEmpty()
    
    val ratingText: String
        get() = rating?.let { 
            val rounded = (it * 10).toInt() / 10.0
            "$rounded"
        } ?: "Sin valoración"
    
    val genresText: String
        get() = genres?.joinToString(", ") ?: ""
    
    val platformsText: String
        get() = platforms?.joinToString(", ") ?: ""
    
    val releaseDateText: String
        get() {
            if (releaseDate == null) return "Fecha desconocida"
            // Convert Unix timestamp to readable date
            val date = kotlinx.datetime.Instant.fromEpochSeconds(releaseDate)
            return date.toString().substringBefore('T')
        }
}
