package com.readify.bookpublishing.application.service.getbookchapters

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.bookpublishing.application.service.common.ChapterStatus
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookMother
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.ChapterMother
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetBookChaptersServiceShould {
    private val bookRepository: BookRepository = mockk()
    private val chapterRepository: ChapterRepository = mockk()
    private val service = GetBookChaptersService(bookRepository, chapterRepository)

    @Test
    fun `return book not found when book does not exists`() {
        every { bookRepository.findById(BookId(bookId)) } returns null

        val response = service.execute(GetBookChaptersRequest(authorId, bookId))

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `return book not found when book does not belongs to requester author`() {
        every { bookRepository.findById(BookId(bookId)) } returns BookMother().finishedBook(bookId, anotherAuthorId)

        val response = service.execute(GetBookChaptersRequest(authorId, bookId))

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `return book chapters successfully`() {
        val chapters = listOf(ChapterMother().publishedOne(authorId, bookId))
        every { bookRepository.findById(BookId(bookId)) } returns BookMother().finishedBook(bookId, authorId)
        every { chapterRepository.findByBookId(BookId(bookId)) } returns chapters

        val response = service.execute(GetBookChaptersRequest(authorId, bookId))

        response as BookChaptersResponse
        assertThat(response.chapters).hasSize(1)
        assertThat(response.chapters[0].id).isEqualTo(chapters[0].id.value)
        assertThat(response.chapters[0].bookId).isEqualTo(chapters[0].bookId.value)
        assertThat(response.chapters[0].authorId).isEqualTo(chapters[0].authorId.value)
        assertThat(response.chapters[0].title).isEqualTo(chapters[0].title.value)
        assertThat(response.chapters[0].excerpt).isEqualTo(chapters[0].excerpt?.value)
        assertThat(response.chapters[0].modifiedAt).isEqualTo(chapters[0].modifiedAt)
        assertThat(response.chapters[0].status).isEqualTo(ChapterStatus.PUBLISHED)
        assertThat(response.chapters[0].order).isEqualTo(chapters[0].order.value)
        assertThat(response.chapters[0].priceAmount).isEqualTo(chapters[0].price.amount)
        assertThat(response.chapters[0].priceCurrency).isEqualTo(chapters[0].price.currency.toString())
    }

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val anotherAuthorId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
    }
}