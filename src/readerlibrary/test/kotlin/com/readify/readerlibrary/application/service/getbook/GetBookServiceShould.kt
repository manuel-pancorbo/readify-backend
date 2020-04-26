package com.readify.readerlibrary.application.service.getbook

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.book.BookMother
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.chapter.ChapterMother
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetBookServiceShould {
    private val bookRepository: BookRepository = mockk()
    private val chapterRepository: ChapterRepository = mockk()
    private val service = GetBookService(bookRepository, chapterRepository)

    @Test
    fun `return not found when repository returns null`() {
        every { bookRepository.findById(BookId(bookId)) } returns null

        val response = service.execute(GetBookRequest(bookId))

        assertThat(response).isEqualTo(BookNotFound)
    }

    @Test
    fun `return book when repository returns valid book`() {
        val inProgressBook = BookMother().inProgressBook(bookId, authorId)
        val chapter = ChapterMother().firstOne(authorId, bookId)
        every { bookRepository.findById(BookId(bookId)) } returns inProgressBook
        every { chapterRepository.findByBookId(BookId(bookId)) } returns listOf(chapter)

        val response = service.execute(GetBookRequest(bookId))

        assertThat(response).isInstanceOf(BookResponse::class)
        response as BookResponse
        assertThat(response.id).isEqualTo(inProgressBook.id.value)
        assertThat(response.authorId).isEqualTo(inProgressBook.authorId.value)
        assertThat(response.title).isEqualTo(inProgressBook.title.value)
        assertThat(response.cover).isEqualTo(inProgressBook.cover.value)
        assertThat(response.summary).isEqualTo(inProgressBook.summary.value)
        assertThat(response.tags).isEqualTo(inProgressBook.tags.value)
        assertThat(response.priceAmount).isEqualTo(inProgressBook.price.amount)
        assertThat(response.priceCurrency).isEqualTo(inProgressBook.price.currency.toString())
        assertThat(response.completionPercentage).isEqualTo(inProgressBook.completionPercentage.value)
        assertThat(response.status).isEqualTo(BookStatusResponse.IN_PROGRESS)
        assertThat(response.finishedAt).isNull()
        assertThat(response.chapters[0].id).isEqualTo(chapter.id.value)
        assertThat(response.chapters[0].title).isEqualTo(chapter.title.value)
        assertThat(response.chapters[0].priceAmount).isEqualTo(chapter.price.amount)
        assertThat(response.chapters[0].priceCurrency).isEqualTo(chapter.price.currency.toString())
        assertThat(response.chapters[0].order).isEqualTo(chapter.order.value)
        assertThat(response.chapters[0].excerpt).isEqualTo(chapter.excerpt?.value)
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
    }
}