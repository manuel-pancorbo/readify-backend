package com.readify.bookpublishing.application.service.updatebook

import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.CompletionPercentage
import com.readify.bookpublishing.domain.book.CompletionPercentageOutOfRangeException
import com.readify.bookpublishing.domain.book.FinishedBook
import com.readify.bookpublishing.domain.book.InProgressBook
import com.readify.bookpublishing.domain.book.Visibility
import com.readify.shared.domain.event.bus.EventBus

class UpdateBookService(private val bookRepository: BookRepository, private val eventBus: EventBus) {
    fun execute(request: UpdateBookRequest): UpdateBookResponse {
        val book = bookRepository.findById(BookId(request.bookId)) ?: return BookNotFoundResponse
        if (!book.sameAuthor(AuthorId(request.authorId))) return BookNotBelongToAuthorResponse

        return try {
            book.update(CompletionPercentage(request.completionPercentage))
                .also { eventBus.publish(it.pullDomainEvents()) }
                .also { bookRepository.save(it) }
                .toResponse()
        } catch (exception: CompletionPercentageOutOfRangeException) {
            CompletionPercentageOutOfRange
        }
    }
}

private fun Book.toResponse() =
    when (this) {
        is InProgressBook -> BookUpdatedSuccessfully(
            authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
            price.currency.toString(), BookStatus.IN_PROGRESS, visibility.toResponse(), null, completionPercentage.value
        )
        is FinishedBook -> BookUpdatedSuccessfully(
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