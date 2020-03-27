package com.readify.bookpublishing.infrastructure.domain.book

import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.PublishedChapter
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapter
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterDataSource
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterStatus

class JpaChapterRepository(private val jpaChapterDataSource: JpaChapterDataSource) : ChapterRepository {
    override fun save(chapter: Chapter) {
        jpaChapterDataSource.save(chapter.toJpa())
    }
}

private fun Chapter.toJpa() =
    when(this) {
        is DraftChapter -> JpaChapter(id.value, authorId.value, bookId.value, title.value, content.value,
            modifiedAt, null, JpaChapterStatus.DRAFT, price.amount, price.currency.toString())
        is PublishedChapter -> JpaChapter(id.value, authorId.value, bookId.value, title.value, content.value,
            modifiedAt, publishedAt, JpaChapterStatus.PUBLISHED, price.amount, price.currency.toString())
    }
