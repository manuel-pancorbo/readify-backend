package com.readify.bookpublishing.application.service.getauthorbooks

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookMother
import com.readify.bookpublishing.domain.book.BookRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetAuthorBooksServiceShould {
    private val bookRepository: BookRepository = mockk()
    private val service = GetAuthorBooksService(bookRepository)

    @Test
    fun `returns book for the given author`() {
        val book = BookMother().finishedBook(bookId, authorId)
        every { bookRepository.findByAuthorId(AuthorId(authorId)) } returns listOf(book)

        val response = service.execute(GetAuthorBooksRequest(authorId))

        assertThat(response.books).hasSize(1)
        assertThat(response.books[0].bookId).isEqualTo(book.id.value)
    }

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
    }
}