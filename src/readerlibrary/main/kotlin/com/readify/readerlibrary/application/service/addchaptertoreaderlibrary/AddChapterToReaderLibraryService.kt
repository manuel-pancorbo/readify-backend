package com.readify.readerlibrary.application.service.addchaptertoreaderlibrary

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.shared.domain.event.bus.EventBus

class AddChapterToReaderLibraryService(
    private val readerLibraryRepository: ReaderLibraryRepository,
    private val eventBus: EventBus
) {
    fun execute(request: AddChapterToReaderLibraryRequest) =
        findReaderLibrary(request.readerId)
            .add(BookId(request.bookId), ChapterId(request.chapterId))
            .also { readerLibraryRepository.save(it) }
            .also { eventBus.publish(it.pullDomainEvents()) }
            .let { AddChapterToReaderLibraryResponse }

    private fun findReaderLibrary(readerId: String) =
        readerLibraryRepository.findByReaderId(ReaderId(readerId))
            ?: ReaderLibrary.create(ReaderId(readerId))
}

data class AddChapterToReaderLibraryRequest(val bookId: String, val chapterId: String, val readerId: String)
object AddChapterToReaderLibraryResponse