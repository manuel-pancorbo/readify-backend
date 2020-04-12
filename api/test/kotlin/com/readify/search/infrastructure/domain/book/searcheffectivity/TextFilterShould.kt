package com.readify.search.infrastructure.domain.book.searcheffectivity

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.readify.IntegrationTest
import com.readify.search.domain.book.AuthorFilter
import com.readify.search.domain.book.BookSearchResult
import com.readify.search.domain.book.SearchCriteria
import com.readify.search.domain.book.TagFilter
import com.readify.search.domain.book.TextFilter
import com.readify.search.infrastructure.domain.book.BookMother
import com.readify.search.infrastructure.domain.book.ElasticSearchBookRepository
import io.searchbox.client.JestClient
import io.searchbox.indices.DeleteIndex
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class TextFilterShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: ElasticSearchBookRepository

    @Autowired
    private lateinit var client: JestClient

    @BeforeEach
    fun setUp() {
        client.execute(DeleteIndex.Builder("books").build())
    }

    @Test
    fun `return results when requested author does match with some book`() {
        val greatBook = BookMother().finishedBook(greatBookId, authorId, title = "Harry Potter y la cámara secreta")
        val matchingBook = BookMother().finishedBook(matchingBookId, authorId, title = "Harry y la historia de la familia Potter")
        val notMatchingBook = BookMother().finishedBook(notMatchingBookId, authorId, title = "La sombra del viento", summary = "La sombra del viento")

        repository.save(greatBook)
        repository.save(matchingBook)
        repository.save(notMatchingBook)
        val results = repository.search(SearchCriteria(textFilter = TextFilter("harry potter")))

        assertThat(results.total).isEqualTo(2)
        assertThat(results.results).hasSize(2)
        assertThat(results.results[0].bookId.value).isEqualTo(greatBookId)
        assertThat(results.results[1].bookId.value).isEqualTo(matchingBookId)
    }

    companion object {
        private val greatBookId = UUID.randomUUID().toString()
        private val matchingBookId = UUID.randomUUID().toString()
        private val notMatchingBookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
    }
}