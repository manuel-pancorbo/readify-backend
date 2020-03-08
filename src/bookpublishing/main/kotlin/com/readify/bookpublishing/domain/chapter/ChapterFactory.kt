package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.bus.EventBus
import java.util.UUID

class ChapterFactory(private val eventBus: EventBus) {
    fun create(authorId: AuthorId, bookId: BookId, title: String, content: String) =
        Chapter.create(Title(title), Content(content), authorId, bookId)
            .also { eventBus.publish(it.pullDomainEvents()) }
}