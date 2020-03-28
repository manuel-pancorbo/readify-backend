package com.readify.bookpublishing.application.service.updatebook

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookMother
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.shared.domain.event.bus.EventBus
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class UpdateBookServiceShould {

    private val bookRepository: BookRepository = mockk(relaxed = true)
    private val eventBus: EventBus = mockk(relaxed = true)
    private val service = UpdateBookService(bookRepository, eventBus)

    @Test
    fun `returns book not belongs to user when an user wants to update another user's book`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val otherAuthor = UUID.randomUUID().toString()
        val request = UpdateBookRequest(anyAuthor, anyBookId, 50)
        every { bookRepository.findById(BookId(anyBookId)) } returns BookMother().inProgressBook(anyBookId, otherAuthor)

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookNotBelongToAuthorResponse)
    }

    @Test
    fun `returns book not found when an user wants to update a non existent book`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val request = UpdateBookRequest(anyAuthor, anyBookId, 50)
        every { bookRepository.findById(BookId(anyBookId)) } returns null

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `returns completion percentage out of range response when given completion percentage is invalid`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val request = UpdateBookRequest(anyAuthor, anyBookId, 101)
        every { bookRepository.findById(BookId(anyBookId)) } returns BookMother().inProgressBook(anyBookId, anyAuthor)

        val response = service.execute(request)

        assertThat(response).isEqualTo(CompletionPercentageOutOfRange)
    }

    @Test
    fun `returns successful response when completion percentage is updated successfully`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val request = UpdateBookRequest(anyAuthor, anyBookId, 75)
        every { bookRepository.findById(BookId(anyBookId)) } returns BookMother().inProgressBook(anyBookId, anyAuthor)

        val response = service.execute(request)

        verify { bookRepository.save(BookMother().inProgressBook(anyBookId, anyAuthor, 75)) }
        verify { eventBus.publish(any()) }
        assertThat(response).isInstanceOf(BookUpdatedSuccessfully::class)
        assertThat((response as BookUpdatedSuccessfully).bookId).isEqualTo(anyBookId)
        assertThat(response.authorId).isEqualTo(anyAuthor)
        assertThat(response.completionPercentage).isEqualTo(75)
    }
}