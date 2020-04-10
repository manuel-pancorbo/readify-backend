package com.readify.readerlibrary.infrastructure.domain.chapter

import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaChapter
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaChapterDataSource

class JpaChapterRepository(private val jpaChapterDataSource: ReaderLibraryJpaChapterDataSource) : ChapterRepository {
    override fun save(chapter: Chapter) {
        jpaChapterDataSource.save(chapter.toJpa())
    }
}

private fun Chapter.toJpa() =
    JpaChapter(
        id.value, authorId.value, bookId.value, title.value, content.value, modifiedAt.toInstant(),
        publishedAt?.toInstant(), price.amount, price.currency.toString(), order.value, excerpt?.value
    )