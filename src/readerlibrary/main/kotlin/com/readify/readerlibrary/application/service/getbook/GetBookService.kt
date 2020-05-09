package com.readify.readerlibrary.application.service.getbook

import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.shared.domain.book.Status

class GetBookService(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository
) {
    fun execute(request: GetBookRequest) =
        bookRepository.findById(BookId(request.id))
            ?.let { it.toResponse(chapterRepository.findByBookId(it.id).sortedBy { chapter -> chapter.order.value }) }
            ?: BookNotFound
}

private fun Book.toResponse(chapters: List<Chapter>) =
    BookResponse(
        id.value, authorId.value, title.value, cover.value, summary.value, tags.value, price.amount,
        price.currency.toString(), completionPercentage.value, status.toResponse(), chapters.map { it.toResponse() },
        finishedAt
    )

private fun Status.toResponse() = when (this) {
    Status.IN_PROGRESS -> BookStatusResponse.IN_PROGRESS
    Status.FINISHED -> BookStatusResponse.FINISHED
}

private fun Chapter.toResponse() =
    ChapterResponse(id.value, title.value, price.amount, price.currency.toString(), order.value, excerpt?.value)
