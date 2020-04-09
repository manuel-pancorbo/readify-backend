package com.readify.bookpublishing.application.service.getbook

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookMother
import com.readify.bookpublishing.domain.book.BookRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetBookServiceShould {
    private val bookRepository: BookRepository = mockk()
    private val service = GetBookService(bookRepository)

    @Test
    fun `return book not found when book does not exists`() {
        every { bookRepository.findById(BookId(bookId)) } returns null

        val response = service.execute(GetBookRequest(authorId, bookId))

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `return book not found when book does not belongs to requester author`() {
        every { bookRepository.findById(BookId(bookId)) } returns BookMother().inProgressBook(bookId, anotherAuthorId)

        val response = service.execute(GetBookRequest(authorId, bookId))

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `return book response successfully when requested book is found`() {
        val book = BookMother().finishedBook(bookId, authorId)
        every { bookRepository.findById(BookId(bookId)) } returns book

        val response = service.execute(GetBookRequest(authorId, bookId))

        response as BookFoundResponse
        assertThat(response.authorId).isEqualTo(book.authorId.value)
        assertThat(response.bookId).isEqualTo(book.id.value)
        assertThat(response.title).isEqualTo(book.title.value)
        assertThat(response.summary).isEqualTo(book.summary.value)
        assertThat(response.cover).isEqualTo(book.cover.value)
        assertThat(response.tags).isEqualTo(book.tags.value)
        assertThat(response.completionPercentage).isEqualTo(book.completionPercentage.value)
        assertThat(response.status).isEqualTo(BookStatus.FINISHED)
        assertThat(response.visibility).isEqualTo(BookVisibility.NULL)
        assertThat(response.priceAmount).isEqualTo(book.price.amount)
        assertThat(response.priceCurrency).isEqualTo(book.price.currency.toString())
        assertThat(response.finishedAt).isEqualTo(book.finishedAt)
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
        private val anotherAuthorId = UUID.randomUUID().toString()
    }
}