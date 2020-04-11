package com.readify.readerlibrary.application.service.addbooktoreaderlibrary

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository

class AddBookToReaderLibraryService(private val readerLibraryRepository: ReaderLibraryRepository) {
    fun execute(request: AddBookToReaderLibraryRequest) =
        findReaderLibrary(request.readerId)
            .add(LibraryWholeBook(BookId(request.bookId)))
            .also { readerLibraryRepository.save(it) }
            .let { AddBookToReaderLibraryResponse }

    private fun findReaderLibrary(readerId: String) =
        readerLibraryRepository.findByReaderId(ReaderId(readerId))
            ?: ReaderLibrary.create(ReaderId(readerId))
}

data class AddBookToReaderLibraryRequest(val bookId: String, val readerId: String)
object AddBookToReaderLibraryResponse