package com.readify.readerlibrary.infrastructure.eventsubscriber

import com.readify.readerlibrary.domain.book.AuthorId
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.chapter.Content
import com.readify.readerlibrary.domain.chapter.Excerpt
import com.readify.readerlibrary.domain.chapter.Order
import com.readify.readerlibrary.domain.chapter.Title
import com.readify.shared.domain.chapter.Status
import com.readify.shared.domain.event.book.ChapterPublished
import com.readify.shared.domain.event.book.ChapterUpdated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class ChapterSubscriber(private val chapterRepository: ChapterRepository) {
    @EventListener
    fun on(event: ChapterPublished) {
        chapterRepository.save(
            Chapter(
                ChapterId(event.id), Title(event.title), Content(event.content), event.price, AuthorId(event.authorId),
                BookId(event.bookId), event.modifiedAt, Order(event.order), event.excerpt?.let { Excerpt(it) },
                event.publishedAt
            )
        )
    }

    @EventListener
    fun on(event: ChapterUpdated) {
        takeIf { event.status == Status.PUBLISHED }
            .let { chapterRepository.save(Chapter(
                ChapterId(event.id), Title(event.title), Content(event.content), event.price, AuthorId(event.authorId),
                BookId(event.bookId), event.modifiedAt, Order(event.order), event.excerpt?.let { Excerpt(it) },
                event.publishedAt!!
            )) }
    }
}
