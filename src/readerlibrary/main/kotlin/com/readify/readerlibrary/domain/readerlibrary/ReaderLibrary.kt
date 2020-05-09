package com.readify.readerlibrary.domain.readerlibrary

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.userlibrary.BookWasBoughtByReader
import com.readify.shared.domain.event.userlibrary.ChapterWasBoughtByReader

data class ReaderLibrary(val readerId: ReaderId, val library: Map<BookId, LibraryBook>) : RootAggregate() {
    fun add(bookId: BookId) =
        when (val existentBook = library[bookId]) {
            is LibraryWholeBook -> throw IllegalArgumentException()
            is LibraryPartialBook -> copy(library = library.plus(existentBook.bookId to existentBook.complete()))
            null -> copy(library = library.plus(bookId to LibraryWholeBook(bookId)))
        }.also { it.record(BookWasBoughtByReader(it.readerId.value, bookId.value)) }

    fun add(bookId: BookId, chapterId: ChapterId) =
        when (val existentBook = library[bookId]) {
            is LibraryWholeBook -> throw IllegalArgumentException()
            is LibraryPartialBook -> copy(
                library = library.plus(
                    existentBook.bookId to existentBook.addChapter(
                        chapterId
                    )
                )
            )
            null -> copy(library = library.plus(bookId to LibraryPartialBook(bookId, listOf(chapterId))))
        }.also { it.record(ChapterWasBoughtByReader(it.readerId.value, bookId.value, chapterId.value)) }

    fun contains(bookId: BookId, chapterId: ChapterId) =
        when (val book = library[bookId]) {
            is LibraryWholeBook -> true
            is LibraryPartialBook -> book.contains(chapterId)
            null -> false
        }

    companion object {
        fun create(readerId: ReaderId) = ReaderLibrary(readerId, emptyMap())
    }
}

sealed class LibraryBook(open val id: BookId)
data class LibraryWholeBook(val bookId: BookId) : LibraryBook(bookId)
data class LibraryPartialBook(val bookId: BookId, val chapters: List<ChapterId>) : LibraryBook(bookId) {
    fun complete() = LibraryWholeBook(bookId)
    fun addChapter(chapterId: ChapterId) = copy(chapters = chapters.plus(chapterId))
    fun contains(chapterId: ChapterId) = chapters.contains(chapterId)
}