package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.BookId

interface ChapterRepository {
    fun save(chapter: Chapter)
    fun findByIdAndBookId(id: ChapterId, bookId: BookId): Chapter?
}
