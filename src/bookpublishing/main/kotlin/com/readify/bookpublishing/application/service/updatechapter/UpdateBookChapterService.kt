package com.readify.bookpublishing.application.service.updatechapter

import com.readify.bookpublishing.application.service.createchapter.ChapterStatus
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
        if (request.status != "published") return InvalidChapterStatusResponse

        return chapter.publishIfNeeded(eventBus)
            .update(request.title, request.content)
            .also { eventBus.publish(it.pullDomainEvents()) }
            .also { chapterRepository.save(it) }
            .toResponse()
    }
}

private fun Chapter.publishIfNeeded(eventBus: EventBus) =
    if (this is DraftChapter) {
        this.publish()
            .also { eventBus.publish(it.pullDomainEvents()) }
    } else {
        this
    }

private fun Chapter.toResponse() =
    when (this) {
        is DraftChapter -> BookChapterUpdatedResponse(
            id.value, title.value, content.value, modifiedAt,
            authorId.value, bookId.value, ChapterStatus.DRAFT, price.amount, price.currency.toString()
        )
        is PublishedChapter -> BookChapterUpdatedResponse(
            id.value, title.value, content.value, modifiedAt,
            authorId.value, bookId.value, ChapterStatus.PUBLISHED, price.amount, price.currency.toString()
        )
    }
