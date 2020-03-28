package com.readify.bookpublishing.domain.book

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.shared.domain.event.book.BookFinished
import com.readify.shared.domain.event.book.BookUpdated
import org.junit.jupiter.api.Test
import java.util.UUID

class BookShould {
    @Test
    fun `update percentage completion of a in progress book`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val inProgressBook = BookMother().inProgressBook(anyBookId, anyAuthor)

        val updatedBook = inProgressBook.update(BookChanges(completionPercentage = CompletionPercentage(90)))

        assertThat(updatedBook.completionPercentage).isEqualTo(CompletionPercentage(90))
        assertThat(updatedBook.pullDomainEvents()).hasSize(1)
        assertThat(updatedBook.pullDomainEvents()[0]).isInstanceOf(BookUpdated::class)
        assertThat((updatedBook.pullDomainEvents()[0] as BookUpdated).completionPercentage).isEqualTo(90)
    }

    @Test
    fun `update percentage completion of a in progress book to 100 transform it to a finished book`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val inProgressBook = BookMother().inProgressBook(anyBookId, anyAuthor)

        val updatedBook = inProgressBook.update(BookChanges(completionPercentage = CompletionPercentage(100)))

        assertThat(updatedBook).isInstanceOf(FinishedBook::class)
        assertThat(updatedBook.pullDomainEvents()).hasSize(1)
        assertThat(updatedBook.pullDomainEvents()[0]).isInstanceOf(BookFinished::class)
        assertThat((updatedBook.pullDomainEvents()[0] as BookFinished).completionPercentage).isEqualTo(100)
    }

    @Test
    fun `update percentage completion of a in finished book to less than 100 transform it to a in progress book`() {
        val anyBookId = UUID.randomUUID().toString()
        val anyAuthor = UUID.randomUUID().toString()
        val inProgressBook = BookMother().finishedBook(anyBookId, anyAuthor)

        val updatedBook = inProgressBook.update(BookChanges(completionPercentage = CompletionPercentage(90)))

        assertThat(updatedBook).isInstanceOf(InProgressBook::class)
        assertThat(updatedBook.pullDomainEvents()).hasSize(1)
        assertThat(updatedBook.pullDomainEvents()[0]).isInstanceOf(BookUpdated::class)
        assertThat((updatedBook.pullDomainEvents()[0] as BookUpdated).completionPercentage).isEqualTo(90)
    }
}