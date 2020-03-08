package com.readify.bookpublishing.infrastructure.domain.book

import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapter
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterDataSource

class JpaChapterRepository(private val jpaChapterDataSource: JpaChapterDataSource) : ChapterRepository {
    override fun save(chapter: Chapter) {
        jpaChapterDataSource.save(chapter.toJpa())
    }
}

private fun Chapter.toJpa() =
    JpaChapter(id.value, authorId.value, bookId.value, title.value, content.value, modifiedAt)
