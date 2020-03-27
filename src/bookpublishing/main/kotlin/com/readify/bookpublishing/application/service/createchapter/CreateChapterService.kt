package com.readify.bookpublishing.application.service.createchapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterFactory
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.PublishedChapter
import com.readify.shared.domain.money.CurrencyNotSupportedException
import com.readify.shared.domain.money.Money

class CreateChapterService(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val chapterFactory: ChapterFactory
) {
    fun execute(request: CreateChapterRequest): CreateChapterResponse {
        val book = bookRepository.findById(BookId(request.bookId)) ?: return BookNotFoundResponse

        return if (!book.sameAuthor(AuthorId(request.authorId)))
            BookNotBelongToAuthorResponse
        else
            try {
                chapterFactory.create(
                        book.authorId, book.id, request.title, request.content,
                        Money.of(request.priceAmount, request.priceCurrency)
                    )
                    .also { chapterRepository.save(it) }
                    .toResponse()
            } catch (exception: CurrencyNotSupportedException) {
                InvalidCurrencyResponse
            }
    }

    private fun Chapter.toResponse() =
        when (this) {
            is DraftChapter -> ChapterCreatedResponse(
                id.value, title.value, content.value, modifiedAt,
                authorId.value, bookId.value, ChapterStatus.DRAFT, price.amount, price.currency.toString()
            )
            is PublishedChapter -> ChapterCreatedResponse(
                id.value, title.value, content.value, modifiedAt,
                authorId.value, bookId.value, ChapterStatus.PUBLISHED, price.amount, price.currency.toString()
            )
        }
}