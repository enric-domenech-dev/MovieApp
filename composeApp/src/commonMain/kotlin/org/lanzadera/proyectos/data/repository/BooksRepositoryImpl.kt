package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.book.GoogleBooksResponse
import org.lanzadera.proyectos.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val client: HttpClient,
    private val json: Json,
    private val apiKey: String = "",
    private val maxResultsPerQuery: Int = 40
) : BooksRepository {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    override val booksFlow: StateFlow<List<Book>> = _books

    private var lastQuery: String = ""

    private fun mapVolumeToBook(item: org.lanzadera.proyectos.domain.models.book.VolumeItem): Book {
        val info = item.volumeInfo
        val rawThumb = info?.imageLinks?.thumbnail ?: info?.imageLinks?.smallThumbnail
        val normalizedThumb = rawThumb?.let { thumb ->
            when {
                thumb.startsWith("//") -> "https:$thumb"
                thumb.startsWith("http://") -> thumb.replaceFirst("http://", "https://")
                thumb.startsWith("https://") -> thumb
                else -> thumb // leave as-is
            }
        }

        return Book(
            id = item.id,
            title = info?.title,
            authors = info?.authors,
            description = info?.description,
            thumbnail = normalizedThumb,
            publishedDate = info?.publishedDate
        )
    }

    override suspend fun refreshBooks(force: Boolean, query: String) {
        // evita peticiones repetidas si no se fuerza y la query es igual
        println("SYNCRO BooksRepositoryImpl: refreshBooks called with force=$force, query='$query', lastQuery='$lastQuery', currentSize=${_books.value.size}")
        if (!force && query == lastQuery && _books.value.isNotEmpty()) {
            println("SYNCRO BooksRepositoryImpl: skipping fetch, cache valid")
            return
        }
        lastQuery = query
        val safeQuery = if (query.isBlank()) "bestsellers" else query
        val keyPart = if (apiKey.isBlank()) "" else "&key=${apiKey}"
        val path = "/books/v1/volumes?q=${safeQuery.encodeURLParameter()}&maxResults=$maxResultsPerQuery${keyPart}"

        try {
            val text = client.get(path).bodyAsText()
            println("SYNCRO BooksRepositoryImpl: fetched ${text.length} chars from Google Books")
            val dto: GoogleBooksResponse = json.decodeFromString(text)
            val mapped = dto.items?.map { mapVolumeToBook(it) } ?: emptyList()
            _books.value = mapped
            println("SYNCRO BooksRepositoryImpl: mapped ${mapped.size} books")
        } catch (t: Throwable) {
            println("SYNCRO BooksRepositoryImpl: error fetching books: ${t.message}")
            throw t
        }
    }
}

// util extension for basic url encoding
private fun String.encodeURLParameter(): String = this.replace(" ", "+")
