package com.readify.readerlibrary.domain.book

interface BookRepository {
    fun save(book: Book)
}
