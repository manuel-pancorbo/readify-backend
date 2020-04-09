package com.readify.bookpublishing.domain.book

interface BookRepository {
    fun save(book: Book)
    fun findById(id: BookId): Book?
    fun findByAuthorId(authorId: AuthorId): List<Book>
}
