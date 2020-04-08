package com.readify.bookpublishing.application.service.updatechapter

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.bookpublishing.application.service.common.ChapterStatus
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.chapter.ChapterId
import com.readify.bookpublishing.domain.chapter.ChapterMother
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.shared.domain.event.bus.EventBus
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UpdateBookChapterServiceShould {

    private val chapterRepository: ChapterRepository = mockk(relaxed = true)
    private val eventBus: EventBus = mockk(relaxed = true)
    private val service = UpdateBookChapterService(chapterRepository, eventBus)

    @Test
    fun `return book chapter not found when requested chapter does not exists`() {
        val request = UpdateBookChapterRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, ANY_CHAPTER_ID, "published")
        every { chapterRepository.findByIdAndBookId(ChapterId(ANY_CHAPTER_ID), BookId(ANY_BOOK_ID)) } returns null

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookChapterNotFoundResponse)
    }

    @Test
    fun `return book not belong to author response when requested book does not belong to requester author`() {
        val request = UpdateBookChapterRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, ANY_CHAPTER_ID, "published")
        every { chapterRepository.findByIdAndBookId(ChapterId(ANY_CHAPTER_ID), BookId(ANY_BOOK_ID)) }
            .returns(ChapterMother().draftOne(ANY_OTHER_AUTHOR_ID, ANY_BOOK_ID))

        val response = service.execute(request)

        assertThat(response).isEqualTo(BookNotBelongToAuthorResponse)
    }

    @Test
    fun `return updated chapter when chapter has been updated successfully`() {
        val request = UpdateBookChapterRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, ANY_CHAPTER_ID, "published",
            NEW_TITLE, NEW_CONTENT, NEW_ORDER, NEW_EXCERPT)
        every { chapterRepository.findByIdAndBookId(ChapterId(ANY_CHAPTER_ID), BookId(ANY_BOOK_ID)) }
            .returns(ChapterMother().draftOne(ANY_AUTHOR_ID, ANY_BOOK_ID))

        val response = service.execute(request)

        assertThat(response).isInstanceOf(BookChapterUpdatedResponse::class)
        assertThat((response as BookChapterUpdatedResponse).status).isEqualTo(ChapterStatus.PUBLISHED)
        assertThat(response.title).isEqualTo(NEW_TITLE)
        assertThat(response.content).isEqualTo(NEW_CONTENT)
        assertThat(response.order).isEqualTo(NEW_ORDER)
        assertThat(response.excerpt).isEqualTo(NEW_EXCERPT)
        verify(exactly = 1) { chapterRepository.save(any()) }
        verify(exactly = 2) { eventBus.publish(any()) }
    }

    @Test
    fun `return same chapter after trying to publish an already published chapter`() {
        val request = UpdateBookChapterRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, ANY_CHAPTER_ID, "published")
        every { chapterRepository.findByIdAndBookId(ChapterId(ANY_CHAPTER_ID), BookId(ANY_BOOK_ID)) }
            .returns(ChapterMother().publishedOne(ANY_AUTHOR_ID, ANY_BOOK_ID))

        val response = service.execute(request)

        assertThat(response).isInstanceOf(BookChapterUpdatedResponse::class)
        assertThat((response as BookChapterUpdatedResponse).status).isEqualTo(ChapterStatus.PUBLISHED)
        verify(exactly = 1) { chapterRepository.save(any()) }
        verify(exactly = 1) { eventBus.publish(any()) }
    }

    companion object {
        private const val ANY_AUTHOR_ID = "f2d2151a-a32a-4c35-a2c8-b53ed8b07908"
        private const val ANY_OTHER_AUTHOR_ID = "4fa2e51b-2622-4b35-a382-543f7a194a1c"
        private const val ANY_BOOK_ID = "bf806dec-b2e8-44b1-805b-8db748d1439f"
        private const val ANY_CHAPTER_ID = "8fc22ffc-2f1d-4957-a823-fba950b242f5"
        private const val NEW_TITLE = "new chapter title"
        private const val NEW_CONTENT = "new chapter content"
        private const val NEW_ORDER = 2
        private const val NEW_EXCERPT = "new excerpt"
    }
}