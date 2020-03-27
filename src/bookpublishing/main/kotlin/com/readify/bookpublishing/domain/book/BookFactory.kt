package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.event.bus.EventBus
import com.readify.shared.domain.money.Money
import java.util.UUID

class BookFactory(private val eventBus: EventBus) {
    fun create(authorId: AuthorId, title: Title, cover: Cover, summary: Summary, tags: Tags, price: Money) =
        Book.create(BookId(UUID.randomUUID().toString()), authorId, title, cover, summary, tags, price)
            .also { eventBus.publish(it.pullDomainEvents()) }
}
