package com.readify.readerlibrary.application.service.getreaderbookchapter

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.chapter.ChapterMother
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetReaderBookChapterServiceShould {
    private val libraryRepository: ReaderLibraryRepository = mockk()
    private val chapterRepository: ChapterRepository = mockk()
    private val service = GetReaderBookChapterService(libraryRepository, chapterRepository)

    @Test
    fun `return chapter not found when reader does not have the book in its library`() {
        val request = GetReaderBookChapterRequest(readerId, bookId, chapterId)
        every { libraryRepository.findByReaderId(ReaderId(readerId)) } returns ReaderLibrary.create(ReaderId(readerId))

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookChapterDoesNotBelongToReader)
    }

    @Test
    fun `return chapter when reader have the chapter in its library`() {
        val request = GetReaderBookChapterRequest(readerId, bookId, chapterId)
        val chapter = ChapterMother().firstOne(authorId, bookId, chapterId)
        val readerLibrary = ReaderLibrary.create(ReaderId(readerId))
            .add(BookId(bookId), ChapterId(chapterId))
        every { libraryRepository.findByReaderId(ReaderId(readerId)) } returns readerLibrary
        every { chapterRepository.findById(ChapterId(chapterId)) } returns chapter

        val response = service.execute(request)

        assertThat(response).isInstanceOf(FullChapterResponse::class)
        response as FullChapterResponse
        assertThat(response.id).isEqualTo(chapter.id.value)
        assertThat(response.content).isEqualTo(chapter.content.value)
        assertThat(response.title).isEqualTo(chapter.title.value)
        assertThat(response.priceAmount).isEqualTo(chapter.price.amount)
        assertThat(response.priceCurrency).isEqualTo(chapter.price.currency.toString())
        assertThat(response.authorId).isEqualTo(chapter.authorId.value)
        assertThat(response.bookId).isEqualTo(chapter.bookId.value)
        assertThat(response.modifiedAt).isEqualTo(chapter.modifiedAt)
        assertThat(response.order).isEqualTo(chapter.order.value)
        assertThat(response.excerpt).isEqualTo(chapter.excerpt?.value)
        assertThat(response.publishedAt).isEqualTo(chapter.publishedAt)
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
    }
}