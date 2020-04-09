package com.readify.bookpublishing.application.service.getauthorbooks

import com.readify.bookpublishing.application.service.common.BookResponse
import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.FinishedBook
import com.readify.bookpublishing.domain.book.InProgressBook
import com.readify.bookpublishing.domain.book.Visibility

class GetAuthorBooksService(private val bookRepository: BookRepository) {
    fun execute(request: GetAuthorBooksRequest) =
        bookRepository.findByAuthorId(AuthorId(request.authorId))
            .map { it.toResponse() }
            .let { GetAuthorBooksResponse(it) }

}

data class GetAuthorBooksRequest(val authorId: String)
data class GetAuthorBooksResponse(val books: List<BookResponse>)

private fun Book.toResponse() =
    when (this) {
        is InProgressBook -> BookResponse(
            authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
            price.currency.toString(), BookStatus.IN_PROGRESS, visibility.toResponse(), null, completionPercentage.value
        )
        is FinishedBook -> BookResponse(
            authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
            price.currency.toString(), BookStatus.FINISHED, visibility.toResponse(), finishedAt,
            completionPercentage.value
        )
    }

private fun Visibility.toResponse() =
    when (this) {
        Visibility.NULL -> BookVisibility.NULL
        Visibility.RESTRICTED -> BookVisibility.RESTRICTED
        Visibility.VISIBLE -> BookVisibility.VISIBLE
    }
