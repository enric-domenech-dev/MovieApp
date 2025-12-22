package org.lanzadera.proyectos.ui.utils

fun normalizeImageUrl(pathOrUrl: String?): String? {
    if (pathOrUrl.isNullOrEmpty()) return null
    val trimmed = pathOrUrl.trim()
    return if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("//")) {
        if (trimmed.startsWith("//")) "https:$trimmed" else trimmed
    } else {
        "https://image.tmdb.org/t/p/w500$trimmed"
    }
}
