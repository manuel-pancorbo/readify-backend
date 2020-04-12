package com.readify.search.application.service.addbook

import com.readify.search.application.service.common.ApplicationBook
import com.readify.search.domain.book.AuthorId
import com.readify.search.domain.book.Book
import com.readify.search.domain.book.BookId
import com.readify.search.domain.book.BookRepository
import com.readify.search.domain.book.BookStatus
import com.readify.search.domain.book.CompletionPercentage
import com.readify.search.domain.book.Cover
import com.readify.search.domain.book.Summary
import com.readify.search.domain.book.Tags
import com.readify.search.domain.book.Title
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class AddBookService(private val bookRepository: BookRepository) {
    fun execute(request: ApplicationBook) = bookRepository.save(request.toDomain())
}

private fun ApplicationBook.toDomain() =
    Book(
        BookId(bookId), AuthorId(authorId), Title(title), Cover(cover), Summary(summary), Tags(tags),
        Money(priceAmount, Currency.valueOf(priceCurrency)), CompletionPercentage(completionPercentage),
        BookStatus.IN_PROGRESS, null
    )
