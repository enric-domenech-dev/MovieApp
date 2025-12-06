package org.lanzadera.proyectos.domain.usecase.books

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.repository.BooksRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RefreshBooksUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: RefreshBooksUseCase
    private lateinit var fakeRepository: FakeBooksRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeRepository = FakeBooksRepository()
        useCase = RefreshBooksUseCase(fakeRepository)
    }
    
    @Test
    fun `should refresh books successfully`() = runTest {
        // Given
        fakeRepository.refreshCalled = false
        
        // When
        val result = useCase.refreshBooks(force = true)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.refreshCalled)
        assertTrue(fakeRepository.forceRefresh)
    }
    
    @Test
    fun `should refresh books with query`() = runTest {
        // Given
        val query = "kotlin programming"
        
        // When
        val result = useCase.refreshBooks(force = false, query = query)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(query, fakeRepository.lastQuery)
    }
    
    @Test
    fun `should refresh fiction books`() = runTest {
        // When
        val result = useCase.refreshFictionBooks(force = true)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.fictionRefreshed)
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureException = Exception("API error")
        
        // When
        val result = useCase.refreshBooks()
        
        // Then
        assertTrue(result.isError)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }
}

class FakeBooksRepository : BooksRepository {
    
    private val _booksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val booksFlow: StateFlow<List<Book>> = _booksFlow
    
    private val _fictionBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val fictionBooksFlow: StateFlow<List<Book>> = _fictionBooksFlow
    
    private val _scienceBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val scienceBooksFlow: StateFlow<List<Book>> = _scienceBooksFlow
    
    private val _historyBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val historyBooksFlow: StateFlow<List<Book>> = _historyBooksFlow
    
    private val _biographyBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val biographyBooksFlow: StateFlow<List<Book>> = _biographyBooksFlow
    
    private val _businessBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val businessBooksFlow: StateFlow<List<Book>> = _businessBooksFlow
    
    private val _technologyBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val technologyBooksFlow: StateFlow<List<Book>> = _technologyBooksFlow
    
    private val _selfHelpBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val selfHelpBooksFlow: StateFlow<List<Book>> = _selfHelpBooksFlow
    
    private val _recentBooksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val recentBooksFlow: StateFlow<List<Book>> = _recentBooksFlow
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    var refreshCalled = false
    var forceRefresh = false
    var lastQuery = ""
    var fictionRefreshed = false
    
    override suspend fun refreshBooks(force: Boolean, query: String) {
        if (shouldFail) throw failureException
        refreshCalled = true
        forceRefresh = force
        lastQuery = query
    }
    
    override suspend fun refreshFictionBooks(force: Boolean) {
        if (shouldFail) throw failureException
        fictionRefreshed = true
    }
    
    override suspend fun refreshScienceBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshHistoryBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshBiographyBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshBusinessBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshTechnologyBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshSelfHelpBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshRecentBooks(force: Boolean) {
        if (shouldFail) throw failureException
    }
}
