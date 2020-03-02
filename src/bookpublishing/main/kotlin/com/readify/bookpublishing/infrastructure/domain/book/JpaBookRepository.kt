package com.readify.bookpublishing.infrastructure.domain.book

import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBook
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookDataSource

class JpaBookRepository(private val jpaBookDataSource: JpaBookDataSource) : BookRepository {
    override fun save(book: Book) {
        jpaBookDataSource.save(book.toJpa())
    }
}

private fun Book.toJpa() = JpaBook(id.value, authorId.value, title.value, cover.value, summary.value)
