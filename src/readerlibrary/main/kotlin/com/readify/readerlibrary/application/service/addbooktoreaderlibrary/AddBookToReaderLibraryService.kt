package com.readify.readerlibrary.application.service.addbooktoreaderlibrary

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.shared.domain.event.bus.EventBus

class AddBookToReaderLibraryService(
    private val readerLibraryRepository: ReaderLibraryRepository,
    private val eventBus: EventBus
) {
    fun execute(request: AddBookToReaderLibraryRequest) =
        findReaderLibrary(request.readerId)
            .add(LibraryWholeBook(BookId(request.bookId)))
            .also { readerLibraryRepository.save(it) }
            .also { eventBus.publish(it.pullDomainEvents()) }
            .let { AddBookToReaderLibraryResponse }

    private fun findReaderLibrary(readerId: String) =
        readerLibraryRepository.findByReaderId(ReaderId(readerId))
            ?: ReaderLibrary.create(ReaderId(readerId))
}

data class AddBookToReaderLibraryRequest(val bookId: String, val readerId: String)
object AddBookToReaderLibraryResponse