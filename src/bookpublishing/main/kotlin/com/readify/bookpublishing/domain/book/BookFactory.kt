package com.readify.bookpublishing.domain.book

import java.util.UUID

class BookFactory {
    fun create(authorId: AuthorId, title: Title, cover: Cover, summary: Summary, tags: Tags) =
        Book.create(BookId(UUID.randomUUID().toString()), authorId, title, cover, summary, tags)
}
