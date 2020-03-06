package com.readify.bookpublishing.infrastructure.jpa.book

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.readify.api.Application
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title
import com.readify.bookpublishing.infrastructure.domain.book.JpaBookRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookDataSource
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(MockKExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaBookRepositoryShould {
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
    }

    private fun anyBook() =
        Book(
            BookId("71ede130-a7d2-4726-8702-90383dc5cd7d"),
            AuthorId("0b35b63a-c7b6-4faf-bda1-c95868def3c7"),
            Title("Harry Potter and the philosopher's stone"),
            Cover("https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"),
            Summary("Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."),
            Tags(listOf("fantasy", "magic"))
        )
}