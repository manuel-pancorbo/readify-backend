package com.readify.readerlibrary.domain.chapter

import com.readify.readerlibrary.domain.book.BookId

interface ChapterRepository {
    fun save(chapter: Chapter)
    fun findById(chapterId: ChapterId): Chapter?
    fun findByBookId(bookId: BookId): List<Chapter>
}