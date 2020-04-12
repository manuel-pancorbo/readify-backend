package com.readify.search.infrastructure.eventsubscriber

import com.readify.search.application.service.addbook.AddBookService
import com.readify.search.application.service.common.ApplicationBook
import com.readify.search.application.service.common.ApplicationBookStatus
import com.readify.shared.domain.book.Status
import com.readify.shared.domain.book.Visibility
import com.readify.shared.domain.event.book.BookCreated
import com.readify.shared.domain.event.book.BookFinished
import com.readify.shared.domain.event.book.BookUpdated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class BookSubscriber(private val addBookService: AddBookService) {
    @EventListener
    fun on(event: BookCreated) =
        event
            .takeIf { it.visibility == Visibility.VISIBLE }
            ?.let {
                addBookService.execute(
                    ApplicationBook(
                        it.id,
                        it.authorId,
                        it.title,
                        it.cover,
                        it.summary,
                        it.tags,
                        it.price.amount,
                        it.price.currency.toString(),
                        it.completionPercentage,
                        ApplicationBookStatus.IN_PROGRESS,
                        null
                    )
                )
            }

    @EventListener
    fun on(event: BookFinished) =
        event
            .takeIf { it.visibility == Visibility.VISIBLE }
            ?.let {
                addBookService.execute(
                    ApplicationBook(
                        it.id,
                        it.authorId,
                        it.title,
                        it.cover,
                        it.summary,
                        it.tags,
                        it.price.amount,
                        it.price.currency.toString(),
                        it.completionPercentage,
                        ApplicationBookStatus.FINISHED,
                        it.finishedAt
                    )
                )
            }

    @EventListener
    fun on(event: BookUpdated) =
        event
            .takeIf { it.visibility == Visibility.VISIBLE }
            ?.let {
                addBookService.execute(
                    ApplicationBook(
                        it.id,
                        it.authorId,
                        it.title,
                        it.cover,
                        it.summary,
                        it.tags,
                        it.price.amount,
                        it.price.currency.toString(),
                        it.completionPercentage,
                        it.status.toApplication(),
                        it.finishedAt
                    )
                )
            }
}

private fun Status.toApplication() =
    when (this) {
        Status.IN_PROGRESS -> ApplicationBookStatus.IN_PROGRESS
        Status.FINISHED -> ApplicationBookStatus.FINISHED
    }
