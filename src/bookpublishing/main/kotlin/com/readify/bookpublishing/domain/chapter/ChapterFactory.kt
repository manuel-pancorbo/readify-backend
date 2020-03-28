package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.event.bus.EventBus
import com.readify.shared.domain.money.Money

class ChapterFactory(private val eventBus: EventBus) {
    fun create(authorId: AuthorId, bookId: BookId, title: String, content: String, price: Money) =
        Chapter.create(Title(title), Content(content), price, authorId, bookId)
            .also { eventBus.publish(it.pullDomainEvents()) }
}