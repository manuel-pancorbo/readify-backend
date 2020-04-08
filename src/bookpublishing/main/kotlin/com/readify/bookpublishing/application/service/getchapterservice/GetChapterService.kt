package com.readify.bookpublishing.application.service.getchapterservice

import com.readify.bookpublishing.application.service.common.ChapterStatus
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterId
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.PublishedChapter

class GetChapterService(private val chapterRepository: ChapterRepository) {
    fun execute(request: GetChapterRequest) =
        chapterRepository.findByIdAndBookId(ChapterId(request.chapterId), BookId(request.bookId))
            ?.takeIf { it.sameAuthor(AuthorId(request.authorId)) }
            ?.toResponse()
            ?: ChapterNotFoundResponse

}

private fun Chapter.toResponse() =
    when (this) {
        is DraftChapter -> ChapterResponse(
            id.value, title.value, content.value, modifiedAt,
            authorId.value, bookId.value, ChapterStatus.DRAFT, price.amount, price.currency.toString(), order.value,
            excerpt?.value
        )
        is PublishedChapter -> ChapterResponse(
            id.value, title.value, content.value, modifiedAt,
            authorId.value, bookId.value, ChapterStatus.PUBLISHED, price.amount, price.currency.toString(), order.value,
            excerpt?.value
        )
    }
