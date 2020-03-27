package com.readify.bookpublishing.application.service.createbook

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title
import com.readify.shared.domain.money.CurrencyNotSupportedException
import com.readify.shared.domain.money.Money.Companion.of

class PublishBookService(private val bookFactory: BookFactory, private val bookRepository: BookRepository) {
    fun execute(request: PublishBookRequest) =
        try {
            bookFactory.create(
                    AuthorId(request.authorId),
                    Title(request.title),
                    Cover(request.cover),
                    Summary(request.summary),
                    Tags(request.tags),
                    of(request.priceAmount, request.priceCurrency)
                )
                .also { bookRepository.save(it) }
                .toResponse()
        } catch (exception: CurrencyNotSupportedException) {
            InvalidCurrencyResponse
        }
}

private fun Book.toResponse() =
    BookPublishedSuccessfullyResponse(
        authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
        price.currency.toString()
    )

data class PublishBookRequest(
    val authorId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String
)

sealed class PublishBookResponse
object InvalidCurrencyResponse : PublishBookResponse()
data class BookPublishedSuccessfullyResponse(
    val authorId: String,
    val bookId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String
) : PublishBookResponse()