package com.readify.readerlibrary.infrastructure.domain.chapter

import com.readify.readerlibrary.domain.book.AuthorId
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.chapter.Content
import com.readify.readerlibrary.domain.chapter.Excerpt
import com.readify.readerlibrary.domain.chapter.Order
import com.readify.readerlibrary.domain.chapter.Title
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaChapter
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaChapterDataSource
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class JpaChapterRepository(private val jpaChapterDataSource: ReaderLibraryJpaChapterDataSource) : ChapterRepository {
    override fun save(chapter: Chapter) {
        jpaChapterDataSource.save(chapter.toJpa())
    }

    override fun findById(chapterId: ChapterId): Chapter? =
        jpaChapterDataSource.findById(chapterId.value)
            .map { it.toDomain() }
            .orElse(null)
}

private fun JpaChapter.toDomain() =
    Chapter(
        ChapterId(id), Title(title), Content(content), Money(priceAmount, Currency.valueOf(priceCurrency)),
        AuthorId(authorId), BookId(bookId), Clock().from(modifiedAt), Order(chapterOrder),
        excerpt?.let { Excerpt(it) }, Clock().from(publishedAt)
    )

private fun Chapter.toJpa() =
    JpaChapter(
        id.value, authorId.value, bookId.value, title.value, content.value, modifiedAt.toInstant(),
        publishedAt.toInstant(), price.amount, price.currency.toString(), order.value, excerpt?.value
    )