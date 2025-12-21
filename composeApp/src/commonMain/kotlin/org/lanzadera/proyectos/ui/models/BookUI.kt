package org.lanzadera.proyectos.ui.models

/**
 * UI model for Book.
 * 
 * Simplified version of domain Book with only fields needed for UI display.
 */
data class BookUI(
    val id: String,
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val thumbnail: String?,
    val publishedDate: String?,
    val averageRating: Double?,
    val ratingsCount: Int?,
    val pageCount: Int?,
    val categories: List<String>?,
    val language: String?,
    val previewLink: String?
) {
    val thumbnailUrl: String
        get() = thumbnail?.replace("http://", "https://") ?: ""
    
    val hasValidThumbnail: Boolean
        get() = !thumbnail.isNullOrEmpty()
    
    val authorsText: String
        get() = authors?.joinToString(", ") ?: "Autor desconocido"
    
    val ratingText: String
        get() = averageRating?.let { 
            val rounded = (it * 10).toInt() / 10.0
            "$rounded" 
        } ?: "Sin valoración"
    
    val pagesText: String
        get() = pageCount?.let { "$it páginas" } ?: ""
    
    val categoriesText: String
        get() = categories?.joinToString(", ") ?: ""
}
