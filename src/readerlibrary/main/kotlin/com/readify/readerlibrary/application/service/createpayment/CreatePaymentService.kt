package com.readify.readerlibrary.application.service.createpayment

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.payment.PaymentAttempt
import com.readify.readerlibrary.domain.payment.PaymentProvider
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.domain.payment.ReaderId

class CreatePaymentService(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val paymentProvider: PaymentProvider,
    private val paymentRepository: PaymentRepository
) {
    fun execute(request: CreatePaymentRequest): CreatePaymentResponse {
        val book = bookRepository.findById(BookId(request.bookId)) ?: return BookNotFoundResponse
        val chapter = request.chapterId
            ?.let { chapterRepository.findById(ChapterId(it)) ?: return ChapterNotFoundResponse }

        if (chapter != null && !chapter.belongsTo(book)) return ChapterNotFoundResponse

        return paymentProvider.start(PaymentAttempt.of(ReaderId(request.readerId), book, chapter))
            .also { paymentRepository.save(it) }
            .let { PaymentCreatedResponse(it.id.value) }
    }
}

data class CreatePaymentRequest(val readerId: String, val bookId: String, val chapterId: String?)
sealed class CreatePaymentResponse
object BookNotFoundResponse : CreatePaymentResponse()
object ChapterNotFoundResponse : CreatePaymentResponse()
data class PaymentCreatedResponse(val id: String) : CreatePaymentResponse()