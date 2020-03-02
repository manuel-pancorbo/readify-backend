package com.readify.bookpublishing.domain.book

interface BookRepository {
    fun save(book: Book)
}
