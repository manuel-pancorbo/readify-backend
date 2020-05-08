package com.readify.bookpublishing.application.service.updatechapter

import com.readify.bookpublishing.application.service.common.ChapterStatus
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterId
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.PublishedChapter
import com.readify.shared.domain.event.bus.EventBus

class UpdateBookChapterService(private val chapterRepository: ChapterRepository, private val eventBus: EventBus) {
    fun execute(request: UpdateBookChapterRequest): UpdateBookChapterResponse {
        val chapter = chapterRepository.findByIdAndBookId(ChapterId(request.chapterId), BookId(request.bookId))
            ?: return BookChapterNotFoundResponse
        if (!chapter.sameAuthor(AuthorId(request.authorId))) return BookNotBelongToAuthorResponse
        if (isStatusInvalid(request.status)) return InvalidChapterStatusResponse

        return chapter.publishIfNeeded(eventBus, request)
            .update(request.title, request.content, request.order, request.excerpt)
            .also { eventBus.publish(it.pullDomainEvents()) }
            .also { chapterRepository.save(it) }
            .toResponse()
    }
}

private fun isStatusInvalid(status: String?) = status !== null && !listOf("published", "draft").contains(status)

private fun Chapter.publishIfNeeded(eventBus: EventBus, request: UpdateBookChapterRequest) =
    if (this is DraftChapter && request.status == "published") {
        this.publish()
            .also { eventBus.publish(it.pullDomainEvents()) }
    } else {
        this
    }

private fun Chapter.toResponse() =
    when (this) {
        is DraftChapter -> BookChapterUpdatedResponse(
            id.value, title.value, content.value, modifiedAt,
            authorId.value, bookId.value, ChapterStatus.DRAFT, price.amount, price.currency.toString(), order.value,
            excerpt?.value
        )
        is PublishedChapter -> BookChapterUpdatedResponse(
            id.value, title.value, content.value, modifiedAt,
            authorId.value, bookId.value, ChapterStatus.PUBLISHED, price.amount, price.currency.toString(), order.value,
            excerpt?.value
        )
    }
