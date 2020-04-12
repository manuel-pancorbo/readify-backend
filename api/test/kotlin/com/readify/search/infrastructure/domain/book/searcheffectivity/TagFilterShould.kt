package com.readify.search.infrastructure.domain.book.searcheffectivity

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.readify.IntegrationTest
import com.readify.search.domain.book.SearchCriteria
import com.readify.search.domain.book.TagFilter
import com.readify.search.infrastructure.domain.book.BookMother
import com.readify.search.infrastructure.domain.book.ElasticSearchBookRepository
import io.searchbox.client.JestClient
import io.searchbox.indices.DeleteIndex
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class TagFilterShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: ElasticSearchBookRepository

    @Autowired
    private lateinit var client: JestClient

    @BeforeEach
    fun setUp() {
        client.execute(DeleteIndex.Builder("books").build())
    }

    @Test
    fun `return results when requested tag does match with some book`() {
        val book = BookMother().finishedBook(bookId, authorId, tags = listOf("magic"))
        val notMatchingBook = BookMother().finishedBook(anotherBookId, authorId, tags = listOf("fantnasy"))

        repository.save(book)
        repository.save(notMatchingBook)
        val results = repository.search(SearchCriteria(tagFilter = TagFilter("magic")))

        assertThat(results.total).isEqualTo(1)
        assertThat(results.results).hasSize(1)
        assertThat(results.results[0].bookId.value).isEqualTo(bookId)
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val anotherBookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
    }
}