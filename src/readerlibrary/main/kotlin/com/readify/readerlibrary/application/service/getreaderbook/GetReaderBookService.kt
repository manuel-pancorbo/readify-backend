package com.readify.readerlibrary.application.service.getreaderbook

import com.readify.readerlibrary.application.service.common.LibraryBookResponse
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse.PARTIAL
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse.WHOLE
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryPartialBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository

class GetReaderBookService(private val repository: ReaderLibraryRepository) {
    fun execute(request: GetReaderBookRequest) =
        if (request.requester != request.reader)
            RequesterAndRequestedReaderAreDifferent
        else
            request.takeIf { request.requester == request.reader }
                ?.let { repository.findByReaderId(ReaderId(request.reader)) }
                ?.library
                ?.get(BookId(request.book))
                ?.let { ReaderBookResponse(it.toResponse()) }
                ?: BookDoesNotBelongToReader

}

private fun LibraryBook.toResponse(): LibraryBookResponse {
    return when (this) {
        is LibraryWholeBook -> LibraryBookResponse(WHOLE, id.value, emptyList())
        is LibraryPartialBook -> LibraryBookResponse(PARTIAL, id.value, chapters.map { it.value })
    }
}
