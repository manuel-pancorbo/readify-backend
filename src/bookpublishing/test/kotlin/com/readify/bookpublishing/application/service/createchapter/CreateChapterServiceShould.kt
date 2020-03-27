package com.readify.bookpublishing.application.service.createchapter

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookMother
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.ChapterFactory
import com.readify.bookpublishing.domain.chapter.ChapterMother
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.shared.domain.money.Money
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class CreateChapterServiceShould {
    private val chapterRepository: ChapterRepository = mockk(relaxed = true)
    private val bookRepository: BookRepository = mockk()
    private val chapterFactory: ChapterFactory = mockk()
    private val service = CreateChapterService(bookRepository, chapterRepository, chapterFactory)

    @Test
    fun `return book not found response when provided book id does not exists`() {
        val bookId = UUID.randomUUID().toString()
        val request = CreateChapterRequestMother().createWith("author-id", bookId)
        every { bookRepository.findById(BookId(bookId)) } returns null

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `return book not belong to author response when provided book id belongs to another author`() {
        val bookId = UUID.randomUUID().toString()
        val request = CreateChapterRequestMother().createWith("author-id", bookId)
        every { bookRepository.findById(BookId(bookId)) }
            .returns(BookMother().validOne(bookId, UUID.randomUUID().toString()))

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookNotBelongToAuthorResponse)
    }

    @Test
    fun `return chapter created response when chapter has been created successfully`() {
        val bookId = UUID.randomUUID().toString()
        val authorId = UUID.randomUUID().toString()
        val request = CreateChapterRequestMother().createWith(authorId, bookId)
        val expectedChapter = ChapterMother().draftOne(authorId, bookId)
        every { bookRepository.findById(BookId(bookId)) } returns BookMother().validOne(bookId, authorId)
        every { chapterFactory.create(AuthorId(authorId), BookId(bookId), request.title, request.content,
            Money.of(request.priceAmount, request.priceCurrency)) }
            .returns(expectedChapter)

        val response = service.execute(request)

        verify { chapterRepository.save(expectedChapter) }
        assertThat(response).isEqualTo(
            ChapterCreatedResponse(
                expectedChapter.id.value,
                expectedChapter.title.value,
                expectedChapter.content.value,
                expectedChapter.modifiedAt,
                expectedChapter.authorId.value,
                expectedChapter.bookId.value,
                ChapterStatus.DRAFT,
                1.3f,
                "EUR"
            )
        )
    }
}