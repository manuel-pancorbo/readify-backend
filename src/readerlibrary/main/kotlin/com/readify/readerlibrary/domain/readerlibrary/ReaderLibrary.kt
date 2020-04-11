package com.readify.readerlibrary.domain.readerlibrary

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.shared.domain.event.RootAggregate

data class ReaderLibrary(val readerId: ReaderId, val library: Map<BookId, LibraryBook>) : RootAggregate() {
    fun add(book: LibraryWholeBook) =
        when (val existentBook = library[book.bookId]) {
            is LibraryWholeBook -> throw IllegalArgumentException()
            is LibraryPartialBook -> copy(library = library.plus(existentBook.bookId to existentBook.complete()))
            null -> copy(library = library.plus(book.bookId to book))
        }

    fun add(book: LibraryPartialBook) =
        when (val existentBook = library[book.bookId]) {
            is LibraryWholeBook -> throw IllegalArgumentException()
            is LibraryPartialBook -> copy(library = library.plus(existentBook.bookId to existentBook.addChapter(book.chapters)))
            null -> copy(library = library.plus(book.bookId to book))
        }

    companion object {
        fun create(readerId: ReaderId) = ReaderLibrary(readerId, emptyMap())
    }
}

sealed class LibraryBook(open val id: BookId)
data class LibraryWholeBook(val bookId: BookId) : LibraryBook(bookId)
data class LibraryPartialBook(val bookId: BookId, val chapters: List<ChapterId>) : LibraryBook(bookId) {
    fun complete() = LibraryWholeBook(bookId)
    fun addChapter(chapters: List<ChapterId>) = copy(chapters = chapters.plus(chapters))
}