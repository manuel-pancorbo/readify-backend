package com.readify.readerlibrary.application.service.getreaderbooks

import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.shared.domain.book.Status

class GetReaderBooksService(
    private val repository: ReaderLibraryRepository,
    private val bookRepository: BookRepository
) {
    fun execute(request: GetReaderBooksRequest) =
        request.takeIf { it.reader == it.requester }
            ?.let { repository.findByReaderId(ReaderId(request.reader))?.library?.values ?: emptyList() }
            ?.let { bookRepository.findByIds(it.map { libraryBook -> libraryBook.id }) }
            ?.map { it.toResponse() }
            ?.let { ReaderBooksResponse(it) }
            ?: RequesterAndRequestedReaderAreDifferent

}

private fun Book.toResponse() =
    BookResponse(
        id.value,
        authorId.value,
        title.value,
        cover.value,
        summary.value,
        tags.value,
        price.amount,
        price.currency.toString(),
        completionPercentage.value,
        status.toResponse(),
        finishedAt
    )

private fun Status.toResponse() = when (this) {
    Status.IN_PROGRESS -> BookStatusResponse.IN_PROGRESS
    Status.FINISHED -> BookStatusResponse.FINISHED
}
