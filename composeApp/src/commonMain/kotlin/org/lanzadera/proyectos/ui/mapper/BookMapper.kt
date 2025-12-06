package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.ui.models.BookUI

/**
 * Maps domain Book to UI BookUI.
 */
fun Book.toUI(): BookUI = BookUI(
    id = id ?: "",
    title = title ?: "",
    authors = authors,
    description = description,
    thumbnail = thumbnail,
    publishedDate = publishedDate,
    averageRating = null, // Not in domain model
    ratingsCount = null,  // Not in domain model
    pageCount = null,     // Not in domain model
    categories = null,    // Not in domain model
    language = null,      // Not in domain model
    previewLink = null    // Not in domain model
)

/**
 * Maps list of domain Books to UI BookUIs.
 */
fun List<Book>.toUI(): List<BookUI> = map { it.toUI() }
