package com.readify.readerlibrary.infrastructure.eventsubscriber

import com.readify.readerlibrary.domain.book.AuthorId
import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.book.CompletionPercentage
import com.readify.readerlibrary.domain.book.Cover
import com.readify.readerlibrary.domain.book.Summary
import com.readify.readerlibrary.domain.book.Tags
import com.readify.readerlibrary.domain.book.Title
import com.readify.shared.domain.book.Status
import com.readify.shared.domain.book.Visibility
import com.readify.shared.domain.event.book.BookCreated
import com.readify.shared.domain.event.book.BookFinished
import com.readify.shared.domain.event.book.BookUpdated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class BookSubscriber(private val bookRepository: BookRepository) {
    @EventListener
    fun on(event: BookCreated) =
        event
            .takeIf { it.visibility == Visibility.VISIBLE }
            ?.let {
                bookRepository.save(
                    Book(
                        BookId(it.id),
                        AuthorId(it.authorId),
                        Title(it.title),
                        Cover(it.cover),
                        Summary(it.summary),
                        Tags(it.tags),
                        it.price,
                        CompletionPercentage(it.completionPercentage),
                        Status.IN_PROGRESS,
                        null
                    )
                )
            }

    @EventListener
    fun on(event: BookFinished) =
        event
            .takeIf { it.visibility == Visibility.VISIBLE }
            ?.let {
                bookRepository.save(
                    Book(
                        BookId(it.id),
                        AuthorId(it.authorId),
                        Title(it.title),
                        Cover(it.cover),
                        Summary(it.summary),
                        Tags(it.tags),
                        it.price,
                        CompletionPercentage(it.completionPercentage),
                        Status.FINISHED,
                        it.finishedAt
                    )
                )
            }

    @EventListener
    fun on(event: BookUpdated) =
        event
            .takeIf { it.visibility == Visibility.VISIBLE }
            ?.let {
                bookRepository.save(
                    Book(
                        BookId(it.id),
                        AuthorId(it.authorId),
                        Title(it.title),
                        Cover(it.cover),
                        Summary(it.summary),
                        Tags(it.tags),
                        it.price,
                        CompletionPercentage(it.completionPercentage),
                        it.status,
                        it.finishedAt
                    )
                )
            }
}
