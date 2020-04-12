package com.readify.search.domain.book

interface BookRepository {
    fun save(book: Book)
}
