package com.readify.bookpublishing.infrastructure.domain.book

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.CompletionPercentage
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.FinishedBook
import com.readify.bookpublishing.domain.book.InProgressBook
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title
import com.readify.bookpublishing.domain.book.Visibility
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBook
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookDataSource
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookStatus
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookVisibility
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class JpaBookRepository(private val jpaBookDataSource: JpaBookDataSource) : BookRepository {
    override fun save(book: Book) {
        jpaBookDataSource.save(book.toJpa())
    }

    override fun findById(id: BookId): Book? =
        jpaBookDataSource.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)

    override fun findByAuthorId(authorId: AuthorId) =
        jpaBookDataSource.findByAuthorId(authorId.value)
            .map { it.toDomain() }
}

private fun Book.toJpa() =
    when (this) {
        is InProgressBook -> JpaBook(
            id.value, authorId.value, title.value, cover.value, summary.value, tags.value, price.amount,
            price.currency.toString(), JpaBookStatus.IN_PROGRESS, completionPercentage.value, visibility.toJpa(), null
        )
        is FinishedBook -> JpaBook(
            id.value, authorId.value, title.value, cover.value, summary.value, tags.value, price.amount,
            price.currency.toString(), JpaBookStatus.FINISHED, completionPercentage.value, visibility.toJpa(),
            finishedAt.toInstant()
        )
    }

private fun Visibility.toJpa() =
    when (this) {
        Visibility.NULL -> JpaBookVisibility.NULL
        Visibility.RESTRICTED -> JpaBookVisibility.RESTRICTED
        Visibility.VISIBLE -> JpaBookVisibility.VISIBLE
    }

private fun JpaBook.toDomain() = when (status) {
    JpaBookStatus.IN_PROGRESS -> InProgressBook(
        BookId(id), AuthorId(authorId), Title(title), Cover(cover), Summary(summary),
        Tags(tags), Money(priceAmount, Currency.valueOf(priceCurrency)), CompletionPercentage(completionPercentage)
    )
    JpaBookStatus.FINISHED -> FinishedBook(
        BookId(id), AuthorId(authorId), Title(title), Cover(cover), Summary(summary),
        Tags(tags), Money(priceAmount, Currency.valueOf(priceCurrency)), Visibility.NULL, Clock().from(finishedAt!!)
    )
}
