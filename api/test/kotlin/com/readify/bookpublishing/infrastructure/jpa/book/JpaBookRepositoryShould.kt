package com.readify.bookpublishing.infrastructure.jpa.book

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.readify.IntegrationTest
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.InProgressBook
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title
import com.readify.bookpublishing.infrastructure.domain.book.JpaBookRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookDataSource
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookVisibility
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JpaBookRepositoryShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: JpaBookRepository

    @Autowired
    private lateinit var dataSource: JpaBookDataSource

    @BeforeEach
    fun setUp() {
        dataSource.deleteAll()
    }

    @Test
    fun `save a book`() {
        val book = anyBook()

        repository.save(book)

        val actual = dataSource.findById(book.id.value)
        assertThat(actual.isPresent).isTrue()
        assertThat(actual.get().id).isEqualTo(book.id.value)
        assertThat(actual.get().authorId).isEqualTo(book.authorId.value)
        assertThat(actual.get().title).isEqualTo(book.title.value)
        assertThat(actual.get().cover).isEqualTo(book.cover.value)
        assertThat(actual.get().summary).isEqualTo(book.summary.value)
        assertThat(actual.get().tags).isEqualTo(book.tags.value)
        assertThat(actual.get().priceAmount).isEqualTo(book.price.amount)
        assertThat(actual.get().priceCurrency).isEqualTo(book.price.currency.toString())
        assertThat(actual.get().finishedAt).isNull()
        assertThat(actual.get().completionPercentage).isEqualTo(0)
        assertThat(actual.get().visibility).isEqualTo(JpaBookVisibility.NULL)
    }

    @Test
    fun `return an existent book`() {
        val book = anyBook()
        repository.save(book)

        val actual = repository.findById(book.id)

        assertThat(actual).isEqualTo(book)
    }

    private fun anyBook() =
        InProgressBook(
            BookId("71ede130-a7d2-4726-8702-90383dc5cd7d"),
            AuthorId("0b35b63a-c7b6-4faf-bda1-c95868def3c7"),
            Title("Harry Potter and the philosopher's stone"),
            Cover("https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"),
            Summary("Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."),
            Tags(listOf("fantasy", "magic")),
            Money(15.5f, Currency.EUR)
        )
}