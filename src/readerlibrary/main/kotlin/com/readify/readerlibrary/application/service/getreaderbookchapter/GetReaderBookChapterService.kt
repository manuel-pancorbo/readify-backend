package com.readify.readerlibrary.application.service.getreaderbookchapter

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository

class GetReaderBookChapterService(
    private val readerLibraryRepository: ReaderLibraryRepository,
    private val chapterRepository: ChapterRepository
) {
    fun execute(request: GetReaderBookChapterRequest): GetReaderBookChapterResponse {
        return readerLibraryRepository.findByReaderId(ReaderId(request.readerId))
            ?.takeIf { it.contains(BookId(request.bookId), ChapterId(request.chapterId)) }
            ?.let { chapterRepository.findById(ChapterId(request.chapterId)) }
            ?.toResponse()
            ?: BookChapterDoesNotBelongToReader
    }

}

private fun Chapter.toResponse() = FullChapterResponse(
    id.value, title.value, content.value, price.amount,
    price.currency.toString(), authorId.value, bookId.value, modifiedAt, order.value, excerpt?.value, publishedAt
)
