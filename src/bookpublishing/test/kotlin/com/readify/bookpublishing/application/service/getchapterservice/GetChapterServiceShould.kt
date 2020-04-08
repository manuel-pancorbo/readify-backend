package com.readify.bookpublishing.application.service.getchapterservice

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.bookpublishing.application.service.common.ChapterStatus
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.chapter.ChapterId
import com.readify.bookpublishing.domain.chapter.ChapterMother
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetChapterServiceShould {

    private val chapterRepository: ChapterRepository = mockk()
    private val service = GetChapterService(chapterRepository)

    @Test
    fun `return chapter not found when requested book or chapter does not exists`() {
        every { chapterRepository.findByIdAndBookId(ChapterId(chapterId), BookId(bookId)) } returns null

        val response = service.execute(GetChapterRequest(authorId, bookId, chapterId))

        assertThat(response).isEqualTo(ChapterNotFoundResponse)
    }

    @Test
    fun `return chapter not found when requested chapter belongs to another author`() {
        every { chapterRepository.findByIdAndBookId(ChapterId(chapterId), BookId(bookId)) }
            .returns(ChapterMother().publishedOne(anotherAuthorId, bookId))

        val response = service.execute(GetChapterRequest(authorId, bookId, chapterId))

        assertThat(response).isEqualTo(ChapterNotFoundResponse)
    }

    @Test
    fun `return chapter information successfully`() {
        val chapter = ChapterMother().publishedOne(authorId, bookId)
        every { chapterRepository.findByIdAndBookId(ChapterId(chapterId), BookId(bookId)) } returns chapter

        val response = service.execute(GetChapterRequest(authorId, bookId, chapterId))

        response as ChapterResponse
        assertThat(response.id).isEqualTo(chapter.id.value)
        assertThat(response.authorId).isEqualTo(chapter.authorId.value)
        assertThat(response.bookId).isEqualTo(chapter.bookId.value)
        assertThat(response.order).isEqualTo(chapter.order.value)
        assertThat(response.title).isEqualTo(chapter.title.value)
        assertThat(response.content).isEqualTo(chapter.content.value)
        assertThat(response.excerpt).isEqualTo(chapter.excerpt?.value)
        assertThat(response.status).isEqualTo(ChapterStatus.PUBLISHED)
        assertThat(response.priceAmount).isEqualTo(chapter.price.amount)
        assertThat(response.priceCurrency).isEqualTo(chapter.price.currency.toString())
        assertThat(response.modifiedAt).isEqualTo(chapter.modifiedAt)
    }

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val anotherAuthorId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
    }
}