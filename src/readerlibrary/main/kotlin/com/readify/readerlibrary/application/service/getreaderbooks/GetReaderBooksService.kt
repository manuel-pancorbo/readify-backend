package com.readify.readerlibrary.application.service.getreaderbooks

import com.readify.readerlibrary.application.service.common.LibraryBookResponse
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse.PARTIAL
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse.WHOLE
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryPartialBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository

class GetReaderBooksService(private val repository: ReaderLibraryRepository) {
    fun execute(request: GetReaderBooksRequest) =
        request.takeIf { it.reader == it.requester }
            ?.let { repository.findByReaderId(ReaderId(request.reader))?.library?.values ?: emptyList() }
            ?.map { it.toResponse() }
            ?.let { ReaderBooksResponse(it) }
            ?: RequesterAndRequestedReaderAreDifferent

}

private fun LibraryBook.toResponse(): LibraryBookResponse {
    return when (this) {
        is LibraryWholeBook -> LibraryBookResponse(WHOLE, id.value, emptyList())
        is LibraryPartialBook -> LibraryBookResponse(PARTIAL, id.value, chapters.map { it.value })
    }
}