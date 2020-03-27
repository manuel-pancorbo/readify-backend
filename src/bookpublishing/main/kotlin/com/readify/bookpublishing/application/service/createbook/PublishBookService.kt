package com.readify.bookpublishing.application.service.createbook

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.DraftBook
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.FinishedBook
import com.readify.bookpublishing.domain.book.InProgressBook
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title
import com.readify.shared.domain.money.CurrencyNotSupportedException
import com.readify.shared.domain.money.Money

class PublishBookService(private val bookFactory: BookFactory, private val bookRepository: BookRepository) {
    fun execute(request: PublishBookRequest) =
        try {
            bookFactory.create(
                    AuthorId(request.authorId),
                    Title(request.title),
                    Cover(request.cover),
                    Summary(request.summary),
                    Tags(request.tags),
                    Money.of(request.priceAmount, request.priceCurrency)
                )
                .also { bookRepository.save(it) }
                .toResponse()
        } catch (exception: CurrencyNotSupportedException) {
            InvalidCurrencyResponse
        }
}

private fun Book.toResponse() =
    when (this) {
        is DraftBook -> BookPublishedSuccessfullyResponse(
            authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
            price.currency.toString(), BookStatus.DRAFT, null, 0
        )
        is InProgressBook -> BookPublishedSuccessfullyResponse(
            authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
            price.currency.toString(), BookStatus.IN_PROGRESS, null, 0
        )
        is FinishedBook -> BookPublishedSuccessfullyResponse(
            authorId.value, id.value, title.value, summary.value, cover.value, tags.value, price.amount,
            price.currency.toString(), BookStatus.FINISHED, null, 0
        )
    }
