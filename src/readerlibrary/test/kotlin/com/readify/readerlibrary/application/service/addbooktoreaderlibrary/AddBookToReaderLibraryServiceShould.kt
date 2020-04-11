package com.readify.readerlibrary.application.service.addbooktoreaderlibrary

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryPartialBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.shared.domain.event.bus.EventBus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID

class AddBookToReaderLibraryServiceShould {
    private val readerLibraryRepository: ReaderLibraryRepository = mockk(relaxed = true)
    private val eventBus: EventBus = mockk(relaxed = true)
    private val service = AddBookToReaderLibraryService(readerLibraryRepository, eventBus)

    @Test
    fun `add first book to an empty library`() {
        every { readerLibraryRepository.findByReaderId(ReaderId(readerId)) } returns null
        val expectedLibrary = ReaderLibrary(
            ReaderId(readerId),
            mapOf(BookId(bookId) to LibraryWholeBook(BookId(bookId)))
        )

        val response = service.execute(AddBookToReaderLibraryRequest(bookId, readerId))

        assertThat(response).isEqualTo(AddBookToReaderLibraryResponse)
        verify { readerLibraryRepository.save(expectedLibrary) }
        verify { eventBus.publish(any()) }
    }

    @Test
    fun `add whole book to a library which contains some chapters of that book`() {
        val library = ReaderLibrary(
            ReaderId(readerId),
            mapOf(BookId(bookId) to LibraryPartialBook(BookId(bookId), listOf(ChapterId(chapterId))))
        )
        every { readerLibraryRepository.findByReaderId(ReaderId(readerId)) } returns library
        val expectedLibrary = ReaderLibrary(
            ReaderId(readerId),
            mapOf(BookId(bookId) to LibraryWholeBook(BookId(bookId)))
        )

        val response = service.execute(AddBookToReaderLibraryRequest(bookId, readerId))

        assertThat(response).isEqualTo(AddBookToReaderLibraryResponse)
        verify { readerLibraryRepository.save(expectedLibrary) }
        verify { eventBus.publish(any()) }
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
    }
}