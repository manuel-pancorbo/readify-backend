package com.readify.bookpublishing.application.service.createbook

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title

class PublishBookService(private val bookFactory: BookFactory, private val bookRepository: BookRepository) {
    fun execute(request: PublishBookRequest) =
        bookFactory.create(
            AuthorId(request.authorId),
            Title(request.title),
            Cover(request.cover),
            Summary(request.summary),
            Tags(request.tags)
        )
            .also { bookRepository.save(it) }
            .toResponse()
}

private fun Book.toResponse() =
    PublishBookResponse(authorId.value, id.value, title.value, summary.value, cover.value, tags.value, 0f, "")

data class PublishBookRequest(
    val authorId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String
)

data class PublishBookResponse(
    val authorId: String,
    val bookId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String
)