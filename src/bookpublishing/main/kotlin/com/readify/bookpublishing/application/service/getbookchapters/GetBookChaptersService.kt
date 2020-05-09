package com.readify.bookpublishing.application.service.getbookchapters

import com.readify.bookpublishing.application.service.common.ChapterStatus
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.PublishedChapter

class GetBookChaptersService(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository
) {
    fun execute(request: GetBookChaptersRequest) =
        bookRepository.findById(BookId(request.bookId))
            ?.takeIf { it.isWrittenBy(AuthorId(request.authorId)) }
            ?.let { chapterRepository.findByBookId(it.id) }
            ?.let {it.sortedBy { chapter -> chapter.order.value }}
            ?.let { BookChaptersResponse(it.map { chapter -> chapter.toResponse() }) }
            ?: BookNotFoundResponse

}

private fun Chapter.toResponse() =
    when (this) {
        is DraftChapter -> ChapterSummaryResponse(
            id.value, title.value, modifiedAt, authorId.value, bookId.value, ChapterStatus.DRAFT, price.amount,
            price.currency.toString(), order.value, excerpt?.value
        )
        is PublishedChapter -> ChapterSummaryResponse(
            id.value, title.value, modifiedAt, authorId.value, bookId.value, ChapterStatus.PUBLISHED, price.amount,
            price.currency.toString(), order.value, excerpt?.value
        )
    }
