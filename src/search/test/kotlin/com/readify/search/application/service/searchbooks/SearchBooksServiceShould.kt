package com.readify.search.application.service.searchbooks

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.search.application.service.common.ApplicationBookStatus
import com.readify.search.domain.book.AuthorFilter
import com.readify.search.domain.book.BookRepository
import com.readify.search.domain.book.BookSearchResult
import com.readify.search.domain.book.TagFilter
import com.readify.search.domain.book.TextFilter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class SearchBooksServiceShould {
    private val bookRepository: BookRepository = mockk()
    private val service = SearchBooksService(bookRepository)

    @Test
    fun `return books returned by repository`() {
        val request = SearchBooksRequest(text = "harry potter", tag = "fantasy", authorId = authorId)
        val filters = listOf(TextFilter("harry potter"), TagFilter("fantasy"), AuthorFilter(authorId))
        val searchResult = BookSearchResult(5, listOf(BookMother().finishedOne()))
        every { bookRepository.search(filters) } returns searchResult

        val response = service.execute(request)

        assertThat(response.total).isEqualTo(searchResult.total)
        assertThat(response.results[0].bookId).isEqualTo(searchResult.results[0].bookId.value)
        assertThat(response.results[0].authorId).isEqualTo(searchResult.results[0].authorId.value)
        assertThat(response.results[0].title).isEqualTo(searchResult.results[0].title.value)
        assertThat(response.results[0].cover).isEqualTo(searchResult.results[0].cover.value)
        assertThat(response.results[0].summary).isEqualTo(searchResult.results[0].summary.value)
        assertThat(response.results[0].tags).isEqualTo(searchResult.results[0].tags.value)
        assertThat(response.results[0].priceAmount).isEqualTo(searchResult.results[0].price.amount)
        assertThat(response.results[0].priceCurrency).isEqualTo(searchResult.results[0].price.currency.toString())
        assertThat(response.results[0].completionPercentage).isEqualTo(searchResult.results[0].completionPercentage.value)
        assertThat(response.results[0].status).isEqualTo(ApplicationBookStatus.FINISHED)
        assertThat(response.results[0].finishedAt).isEqualTo(searchResult.results[0].finishedAt)
    }

    companion object {
        private val authorId = UUID.randomUUID().toString()
    }
}