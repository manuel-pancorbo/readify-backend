package com.readify.search.infrastructure.domain.book

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.readify.IntegrationTest
import com.readify.shared.domain.clock.Clock
import io.searchbox.client.JestClient
import io.searchbox.core.Get
import io.searchbox.indices.DeleteIndex
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.ZonedDateTime
import java.util.UUID

class ElasticSearchBookRepositoryShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: ElasticSearchBookRepository

    @Autowired
    private lateinit var client: JestClient

    @BeforeEach
    fun setUp() {
        client.execute(DeleteIndex.Builder("books").build())
    }

    @Test
    fun `save a book`() {
        val book = BookMother().finishedBook(bookId, authorId)

        repository.save(book)

        val actual = client.execute(Get.Builder("books", bookId).build())
            .let { it?.getSourceAsObject(ElasticSearchBook::class.java) }
        assertThat(actual).isNotNull()
        actual as ElasticSearchBook
        assertThat(actual.bookId).isEqualTo(book.bookId.value)
        assertThat(actual.authorId).isEqualTo(book.authorId.value)
        assertThat(actual.title).isEqualTo(book.title.value)
        assertThat(actual.cover).isEqualTo(book.cover.value)
        assertThat(actual.summary).isEqualTo(book.summary.value)
        assertThat(actual.tags).isEqualTo(book.tags.value)
        assertThat(actual.price.amount).isEqualTo(book.price.amount)
        assertThat(actual.price.currency).isEqualTo(book.price.currency.toString().toLowerCase())
        assertThat(actual.completionPercentage).isEqualTo(book.completionPercentage.value)
        assertThat(actual.status).isEqualTo(book.status.toString().toLowerCase())
        assertThat(Clock().fromUtc(ZonedDateTime.parse(actual.finishedAt))).isEqualTo(book.finishedAt)
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
    }
}