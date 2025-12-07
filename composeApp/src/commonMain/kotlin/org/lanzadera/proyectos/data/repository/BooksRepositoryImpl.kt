package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.book.GoogleBooksResponseDto
import org.lanzadera.proyectos.data.dto.book.VolumeItemDto
import org.lanzadera.proyectos.data.mapper.toDomain
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.repository.BooksRepository
import org.lanzadera.proyectos.utils.Logger

class BooksRepositoryImpl(
    private val client: HttpClient,
    private val json: Json,
    private val apiKey: String = "",
    private val maxResultsPerQuery: Int = 40
) : BooksRepository {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    override val booksFlow: StateFlow<List<Book>> = _books

    private val _fictionBooks = MutableStateFlow<List<Book>>(emptyList())
    override val fictionBooksFlow: StateFlow<List<Book>> = _fictionBooks

    private val _scienceBooks = MutableStateFlow<List<Book>>(emptyList())
    override val scienceBooksFlow: StateFlow<List<Book>> = _scienceBooks

    private val _historyBooks = MutableStateFlow<List<Book>>(emptyList())
    override val historyBooksFlow: StateFlow<List<Book>> = _historyBooks

    private val _biographyBooks = MutableStateFlow<List<Book>>(emptyList())
    override val biographyBooksFlow: StateFlow<List<Book>> = _biographyBooks

    private val _businessBooks = MutableStateFlow<List<Book>>(emptyList())
    override val businessBooksFlow: StateFlow<List<Book>> = _businessBooks

    private val _technologyBooks = MutableStateFlow<List<Book>>(emptyList())
    override val technologyBooksFlow: StateFlow<List<Book>> = _technologyBooks

    private val _selfHelpBooks = MutableStateFlow<List<Book>>(emptyList())
    override val selfHelpBooksFlow: StateFlow<List<Book>> = _selfHelpBooks

    private val _recentBooks = MutableStateFlow<List<Book>>(emptyList())
    override val recentBooksFlow: StateFlow<List<Book>> = _recentBooks

    private var lastQuery: String = ""

    private fun mapVolumeToBook(item: VolumeItemDto): Book {
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
        if (!force && query == lastQuery && _books.value.isNotEmpty()) {
            return
        }
        lastQuery = query
        val safeQuery = if (query.isBlank()) "subject:fiction&printType=books" else query
        fetchAndStore(_books, safeQuery, "refreshBooks")
    }

    override suspend fun refreshFictionBooks(force: Boolean) {
        if (!force && _fictionBooks.value.isNotEmpty()) return
        fetchAndStore(_fictionBooks, "subject:fiction", "refreshFictionBooks")
    }

    override suspend fun refreshScienceBooks(force: Boolean) {
        if (!force && _scienceBooks.value.isNotEmpty()) return
        fetchAndStore(_scienceBooks, "subject:science", "refreshScienceBooks")
    }

    override suspend fun refreshHistoryBooks(force: Boolean) {
        if (!force && _historyBooks.value.isNotEmpty()) return
        fetchAndStore(_historyBooks, "subject:history", "refreshHistoryBooks")
    }

    override suspend fun refreshBiographyBooks(force: Boolean) {
        if (!force && _biographyBooks.value.isNotEmpty()) return
        fetchAndStore(_biographyBooks, "subject:biography", "refreshBiographyBooks")
    }

    override suspend fun refreshBusinessBooks(force: Boolean) {
        if (!force && _businessBooks.value.isNotEmpty()) return
        fetchAndStore(_businessBooks, "subject:business", "refreshBusinessBooks")
    }

    override suspend fun refreshTechnologyBooks(force: Boolean) {
        if (!force && _technologyBooks.value.isNotEmpty()) return
        fetchAndStore(_technologyBooks, "subject:technology", "refreshTechnologyBooks")
    }

    override suspend fun refreshSelfHelpBooks(force: Boolean) {
        if (!force && _selfHelpBooks.value.isNotEmpty()) return
        fetchAndStore(_selfHelpBooks, "subject:self-help", "refreshSelfHelpBooks")
    }

    override suspend fun refreshRecentBooks(force: Boolean) {
        if (!force && _recentBooks.value.isNotEmpty()) return
        fetchAndStore(_recentBooks, "publishedDate:2025", "refreshRecentBooks")
    }

    private suspend fun fetchAndStore(
        stateFlow: MutableStateFlow<List<Book>>,
        query: String,
        methodName: String
    ) {
        try {
            val keyPart = if (apiKey.isBlank()) "" else "&key=${apiKey}"
            val path = "/books/v1/volumes?q=${query.encodeURLParameter()}&maxResults=$maxResultsPerQuery${keyPart}"

            val text = client.get(path).bodyAsText()
            val dto: GoogleBooksResponseDto = json.decodeFromString(text)
            val mapped = dto.items?.map { mapVolumeToBook(it) } ?: emptyList()
            stateFlow.value = mapped
            Logger.d("$methodName fetched ${mapped.size} books", tag = "BooksRepository")
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("$methodName error for query: $query", tag = "BooksRepository", throwable = e)
            stateFlow.value = emptyList()
        }
    }
}

// util extension for basic url encoding
private fun String.encodeURLParameter(): String = this.replace(" ", "+")
