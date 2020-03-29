package com.readify.bookpublishing.infrastructure.domain.book

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterId
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.domain.chapter.Content
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.PublishedChapter
import com.readify.bookpublishing.domain.chapter.Title
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapter
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterDataSource
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterStatus
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class JpaChapterRepository(private val jpaChapterDataSource: JpaChapterDataSource) : ChapterRepository {
    override fun save(chapter: Chapter) {
        jpaChapterDataSource.save(chapter.toJpa())
    }

    override fun findByIdAndBookId(id: ChapterId, bookId: BookId): Chapter? {
        return jpaChapterDataSource
            .findByIdAndBookId(id.value, bookId.value)
            ?.toDomain()
    }
}

private fun JpaChapter.toDomain(): Chapter {
    return when(status) {
        JpaChapterStatus.DRAFT -> DraftChapter(ChapterId(id), Title(title), Content(content), Money(priceAmount,
            Currency.valueOf(priceCurrency)), AuthorId(authorId), BookId(bookId), Clock().from(modifiedAt))
        JpaChapterStatus.PUBLISHED -> PublishedChapter(ChapterId(id), Title(title), Content(content), Money(priceAmount,
            Currency.valueOf(priceCurrency)), AuthorId(authorId), BookId(bookId), Clock().from(modifiedAt),
            Clock().from(publishedAt!!))
    }
}

private fun Chapter.toJpa() =
    when(this) {
        is DraftChapter -> JpaChapter(id.value, authorId.value, bookId.value, title.value, content.value,
            modifiedAt.toInstant(), null, JpaChapterStatus.DRAFT, price.amount, price.currency.toString())
        is PublishedChapter -> JpaChapter(id.value, authorId.value, bookId.value, title.value, content.value,
            modifiedAt.toInstant(), publishedAt.toInstant(), JpaChapterStatus.PUBLISHED, price.amount, price.currency.toString())
    }
