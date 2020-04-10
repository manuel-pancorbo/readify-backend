package com.readify.readerlibrary.infrastructure.domain.book

import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaBook
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaBookStatus
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaBookDataSource
import com.readify.shared.domain.book.Status

class JpaBookRepository(private val jpaBookDataSource: ReaderLibraryJpaBookDataSource) : BookRepository {
    override fun save(book: Book) {
        jpaBookDataSource.save(book.toJpa())
    }
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
