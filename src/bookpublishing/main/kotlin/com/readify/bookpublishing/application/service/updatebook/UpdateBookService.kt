package com.readify.bookpublishing.application.service.updatebook

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.CompletionPercentage
import com.readify.bookpublishing.domain.book.CompletionPercentageOutOfRangeException
import com.readify.shared.domain.event.bus.EventBus

class UpdateBookService(private val bookRepository: BookRepository, private val eventBus: EventBus) {
    fun execute(request: UpdateBookRequest): UpdateBookResponse {
        val book = bookRepository.findById(BookId(request.bookId)) ?: return BookNotFoundResponse
        if (!book.sameAuthor(AuthorId(request.authorId))) return BookNotBelongToAuthorResponse

        return try {
            book.update(CompletionPercentage(request.completionPercentage))
                .also { eventBus.publish(it.pullDomainEvents()) }
                .also { bookRepository.save(it) }
            BookUpdatedSuccessfully
        } catch (exception: CompletionPercentageOutOfRangeException) {
            CompletionPercentageOutOfRange
        }
    }
}

data class UpdateBookRequest(val authorId: String, val bookId: String, val completionPercentage: Int)

sealed class UpdateBookResponse
object BookUpdatedSuccessfully : UpdateBookResponse()
object CompletionPercentageOutOfRange : UpdateBookResponse()
object BookNotBelongToAuthorResponse : UpdateBookResponse()
object BookNotFoundResponse : UpdateBookResponse()
