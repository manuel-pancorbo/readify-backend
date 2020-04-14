package com.readify.readerlibrary.application.service.getreaderbooks

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryPartialBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetReaderBooksServiceShould {
    private val repository: ReaderLibraryRepository = mockk()
    private val service = GetReaderBooksService(repository)

    @Test
    fun `return error when requester is different from requested reader`() {
        val response = service.execute(GetReaderBooksRequest(anotherReaderId, readerId))

        assertThat(response).isEqualTo(RequesterAndRequestedReaderAreDifferent)
    }

    @Test
    fun `return reader books`() {
        val books = mapOf(
            BookId(wholeBookId) to LibraryWholeBook(BookId(wholeBookId)),
            BookId(partialBookId) to LibraryPartialBook(BookId(partialBookId), someChapters)
        )
        every { repository.findByReaderId(ReaderId(readerId)) } returns ReaderLibrary(ReaderId(readerId), books)
        val response = service.execute(GetReaderBooksRequest(readerId, readerId))

        assertThat(response).isInstanceOf(ReaderBooksResponse::class)
        response as ReaderBooksResponse
        assertThat(response.books).hasSize(2)
        assertThat(response.books[0].id).isEqualTo(wholeBookId)
        assertThat(response.books[0].type).isEqualTo(LibraryBookTypeResponse.WHOLE)
        assertThat(response.books[0].chapters).isEmpty()
        assertThat(response.books[1].id).isEqualTo(partialBookId)
        assertThat(response.books[1].type).isEqualTo(LibraryBookTypeResponse.PARTIAL)
        assertThat(response.books[1].chapters).isEqualTo(someChapters.map { it.value })
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val anotherReaderId = UUID.randomUUID().toString()
        private val wholeBookId = UUID.randomUUID().toString()
        private val partialBookId = UUID.randomUUID().toString()
        private val someChapters = listOf(
            ChapterId(UUID.randomUUID().toString()),
            ChapterId(UUID.randomUUID().toString())
        )
    }
}