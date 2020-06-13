package com.readify.readerlibrary.infrastructure.domain.book

import com.readify.readerlibrary.domain.book.AuthorId
import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.book.CompletionPercentage
import com.readify.readerlibrary.domain.book.Cover
import com.readify.readerlibrary.domain.book.Summary
import com.readify.readerlibrary.domain.book.Tags
import com.readify.readerlibrary.domain.book.Title
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaBook
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaBookStatus
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaBookDataSource
import com.readify.shared.domain.book.Status
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class JpaBookRepository(private val jpaBookDataSource: ReaderLibraryJpaBookDataSource) : BookRepository {
    override fun save(book: Book) {
        jpaBookDataSource.save(book.toJpa())
    }

    override fun findById(bookId: BookId): Book? {
        return jpaBookDataSource.findById(bookId.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByIds(books: List<BookId>): List<Book> {
        return jpaBookDataSource.findByIdIsIn(books.map { it.value })
            .map { it.toDomain() }
    }
}

private fun JpaBook.toDomain(): Book {
    return Book(BookId(id), AuthorId(authorId), Title(title), Cover(cover), Summary(summary), Tags(tags),
        Money(priceAmount, Currency.valueOf(priceCurrency)), CompletionPercentage(completionPercentage),
        status.toDomain(), finishedAt?.let { Clock().from(it) })
}

private fun JpaBookStatus.toDomain() =
    when (this) {
        JpaBookStatus.IN_PROGRESS -> Status.IN_PROGRESS
        JpaBookStatus.FINISHED -> Status.FINISHED
    }

private fun Book.toJpa() =
    JpaBook(
        id.value, authorId.value, title.value, cover.value, summary.value, tags.value, price.amount,
        price.currency.toString(), status.toJpaStatus(), completionPercentage.value, finishedAt?.toInstant()
    )

private fun Status.toJpaStatus() =
    when (this) {
        Status.IN_PROGRESS -> JpaBookStatus.IN_PROGRESS
        Status.FINISHED -> JpaBookStatus.FINISHED
    }