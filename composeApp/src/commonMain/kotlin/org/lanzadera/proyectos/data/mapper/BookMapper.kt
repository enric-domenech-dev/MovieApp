package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.book.BookDto
import org.lanzadera.proyectos.data.dto.book.GoogleBooksResponseDto
import org.lanzadera.proyectos.data.dto.book.ImageLinksDto
import org.lanzadera.proyectos.data.dto.book.VolumeInfoDto
import org.lanzadera.proyectos.data.dto.book.VolumeItemDto
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.book.GoogleBooksResponse
import org.lanzadera.proyectos.domain.models.book.ImageLinks
import org.lanzadera.proyectos.domain.models.book.VolumeInfo
import org.lanzadera.proyectos.domain.models.book.VolumeItem

fun GoogleBooksResponseDto.toDomain(): GoogleBooksResponse = GoogleBooksResponse(
    kind = kind,
    totalItems = totalItems,
    items = items?.map { it.toDomain() }
)

fun VolumeItemDto.toDomain(): VolumeItem = VolumeItem(
    id = id,
    volumeInfo = volumeInfo?.toDomain()
)

fun VolumeInfoDto.toDomain(): VolumeInfo = VolumeInfo(
    title = title,
    authors = authors,
    description = description,
    imageLinks = imageLinks?.toDomain(),
    publishedDate = publishedDate
)

fun ImageLinksDto.toDomain(): ImageLinks = ImageLinks(
    smallThumbnail = smallThumbnail,
    thumbnail = thumbnail
)

fun BookDto.toDomain(): Book = Book(
    id = id,
    title = title,
    authors = authors,
    description = description,
    thumbnail = thumbnail,
    publishedDate = publishedDate
)

fun Book.toDto(): BookDto = BookDto(
    id = id,
    title = title,
    authors = authors,
    description = description,
    thumbnail = thumbnail,
    publishedDate = publishedDate
)

fun GoogleBooksResponse.toDto(): GoogleBooksResponseDto = GoogleBooksResponseDto(
    kind = kind,
    totalItems = totalItems,
    items = items?.map { it.toDto() }
)

fun VolumeItem.toDto(): VolumeItemDto = VolumeItemDto(
    id = id,
    volumeInfo = volumeInfo?.toDto()
)

fun VolumeInfo.toDto(): VolumeInfoDto = VolumeInfoDto(
    title = title,
    authors = authors,
    description = description,
    imageLinks = imageLinks?.toDto(),
    publishedDate = publishedDate
)

fun ImageLinks.toDto(): ImageLinksDto = ImageLinksDto(
    smallThumbnail = smallThumbnail,
    thumbnail = thumbnail
)
