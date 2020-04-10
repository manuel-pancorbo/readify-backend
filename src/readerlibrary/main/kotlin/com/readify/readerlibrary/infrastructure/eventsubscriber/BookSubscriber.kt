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
        takeIf { event.visibility == Visibility.VISIBLE }
            .let {
                bookRepository.save(
                    Book(
                        BookId(event.id),
                        AuthorId(event.authorId),
                        Title(event.title),
                        Cover(event.cover),
                        Summary(event.summary),
                        Tags(event.tags),
                        event.price,
                        CompletionPercentage(event.completionPercentage),
                        event.status,
                        null
                    )
                )
            }

    @EventListener
    fun on(event: BookFinished) =
        takeIf { event.visibility == Visibility.VISIBLE }
            .let {
                bookRepository.save(
                    Book(
                        BookId(event.id),
                        AuthorId(event.authorId),
                        Title(event.title),
                        Cover(event.cover),
                        Summary(event.summary),
                        Tags(event.tags),
                        event.price,
                        CompletionPercentage(event.completionPercentage),
                        event.status,
                        event.finishedAt
                    )
                )
            }

    @EventListener
    fun on(event: BookUpdated) =
        takeIf { event.visibility == Visibility.VISIBLE }
            .let {
                bookRepository.save(
                    Book(
                        BookId(event.id),
                        AuthorId(event.authorId),
                        Title(event.title),
                        Cover(event.cover),
                        Summary(event.summary),
                        Tags(event.tags),
                        event.price,
                        CompletionPercentage(event.completionPercentage),
                        event.status,
                        event.finishedAt
                    )
                )
            }
}
