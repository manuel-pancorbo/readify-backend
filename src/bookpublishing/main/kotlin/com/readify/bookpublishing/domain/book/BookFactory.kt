package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.event.book.BookPublished
import com.readify.shared.domain.event.bus.EventBus
import java.util.UUID

class BookFactory(private val eventBus: EventBus) {
    fun create(authorId: AuthorId, title: Title, cover: Cover, summary: Summary, tags: Tags) =
        Book.create(BookId(UUID.randomUUID().toString()), authorId, title, cover, summary, tags)
            .also { eventBus.publish(it.pullDomainEvents()) }
}
