package com.readify.readerlibrary.application.service.getreaderbook

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetReaderBookServiceShould {
    private val repository: ReaderLibraryRepository = mockk()
    private val service = GetReaderBookService(repository)

    @Test
    fun `return error when requester is different from requested reader`() {
        val response = service.execute(GetReaderBookRequest(anotherReaderId, readerId, bookId))

        assertThat(response).isEqualTo(RequesterAndRequestedReaderAreDifferent)
    }


    @Test
    fun `return error when book does not belong to reader`() {
        every { repository.findByReaderId(ReaderId(readerId)) }.returns(ReaderLibrary.create(ReaderId((readerId))))
        val response = service.execute(GetReaderBookRequest(anotherReaderId, readerId, bookId))

        assertThat(response).isEqualTo(RequesterAndRequestedReaderAreDifferent)
    }

    @Test
    fun `return reader book`() {
        val book = LibraryWholeBook(BookId(bookId))
        every { repository.findByReaderId(ReaderId(readerId)) }
            .returns(ReaderLibrary(ReaderId(readerId), mapOf(BookId(bookId) to book)))

        val response = service.execute(GetReaderBookRequest(readerId, readerId, bookId))

        assertThat(response).isInstanceOf(ReaderBookResponse::class)
        response as ReaderBookResponse
        assertThat(response.book.id).isEqualTo(bookId)
        assertThat(response.book.type).isEqualTo(LibraryBookTypeResponse.WHOLE)
        assertThat(response.book.chapters).isEmpty()
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val anotherReaderId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
    }
}