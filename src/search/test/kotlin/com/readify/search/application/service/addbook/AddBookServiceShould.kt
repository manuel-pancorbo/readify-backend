package com.readify.search.application.service.addbook

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
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class AddBookServiceShould {
    private val bookRepository: BookRepository = mockk(relaxed = true)
    private val service = AddBookService(bookRepository)

    @Test
    fun `save book using book repository`() {
        val request = ApplicationBookMother().inProgressOne()
        val expectedBook = Book(BookId(request.bookId), AuthorId(request.authorId), Title(request.title),
            Cover(request.cover), Summary(request.summary), Tags(request.tags),
            Money(request.priceAmount, Currency.valueOf(request.priceCurrency)),
            CompletionPercentage(request.completionPercentage), BookStatus.IN_PROGRESS, null
        )

        service.execute(request)

        verify { bookRepository.save(expectedBook) }
    }
}