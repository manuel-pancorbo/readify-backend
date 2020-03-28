package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.event.book.BookCreated
import com.readify.shared.domain.event.book.BookFinished
import com.readify.shared.domain.event.book.BookStatus
import com.readify.shared.domain.event.book.BookUpdated

class DomainEventFactory {
    companion object {
        fun bookUpdated(book: Book) =
            when (book) {
                is InProgressBook -> BookUpdated(
                    book.id.value, book.authorId.value, book.title.value, book.cover.value, book.summary.value,
                    book.tags.value, book.price, book.completionPercentage.value, book.visibility.toString(),
                    BookStatus.IN_PROGRESS
                )
                is FinishedBook -> BookUpdated(
                    book.id.value, book.authorId.value, book.title.value, book.cover.value, book.summary.value,
                    book.tags.value, book.price, book.completionPercentage.value, book.visibility.toString(),
                    BookStatus.FINISHED, book.finishedAt
                )
            }

        fun bookCreated(book: InProgressBook) =
            BookCreated(
                book.id.value, book.authorId.value, book.title.value, book.cover.value, book.summary.value,
                book.tags.value, book.price, book.completionPercentage.value, book.visibility.toString(),
                BookStatus.IN_PROGRESS
            )

        fun bookFinished(book: FinishedBook) =
            BookFinished(
                book.id.value, book.authorId.value, book.title.value, book.cover.value, book.summary.value,
                book.tags.value, book.price, book.completionPercentage.value, book.visibility.toString(),
                BookStatus.IN_PROGRESS, book.finishedAt
            )
    }
}