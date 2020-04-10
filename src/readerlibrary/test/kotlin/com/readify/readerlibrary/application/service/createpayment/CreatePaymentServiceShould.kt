package com.readify.readerlibrary.application.service.createpayment

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.book.BookMother
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.chapter.ChapterMother
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.PaymentAttempt
import com.readify.readerlibrary.domain.payment.PaymentProvider
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.payment.Status
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID

class CreatePaymentServiceShould {
    private val bookRepository: BookRepository = mockk()
    private val chapterRepository: ChapterRepository = mockk()
    private val paymentProvider: PaymentProvider = mockk()
    private val paymentRepository: PaymentRepository = mockk(relaxed = true)
    private val service = CreatePaymentService(bookRepository, chapterRepository, paymentProvider, paymentRepository)

    @Test
    fun `return book not found when requested book does not exists`() {
        every { bookRepository.findById(BookId(bookId)) } returns null

        val response = service.execute(CreatePaymentRequest(readerId, bookId, chapterId))

        assertThat(response).isEqualTo(BookNotFoundResponse)
    }

    @Test
    fun `return chapter not found when requested chapter does not exists`() {
        every { bookRepository.findById(BookId(bookId)) } returns BookMother().inProgressBook(bookId, authorId)
        every { chapterRepository.findById(ChapterId(chapterId)) } returns null

        val response = service.execute(CreatePaymentRequest(readerId, bookId, chapterId))

        assertThat(response).isEqualTo(ChapterNotFoundResponse)
    }


    @Test
    fun `return chapter not found when requested chapter does not belongs to requested book`() {
        every { bookRepository.findById(BookId(bookId)) } returns BookMother().inProgressBook(bookId, authorId)
        every { chapterRepository.findById(ChapterId(chapterId)) }
            .returns(ChapterMother().firstOne(authorId, anotherBookId))

        val response = service.execute(CreatePaymentRequest(readerId, bookId, chapterId))

        assertThat(response).isEqualTo(ChapterNotFoundResponse)
    }


    @Test
    fun `return success payment it for a whole book payment`() {
        val book = BookMother().inProgressBook(bookId, authorId)
        val payment = wholeBookPayment()
        every { bookRepository.findById(BookId(bookId)) } returns book
        every { paymentProvider.start(PaymentAttempt.of(ReaderId(readerId), book, null)) } returns payment

        val response = service.execute(CreatePaymentRequest(readerId, bookId, null))

        response as PaymentCreatedResponse
        assertThat(response.id).isEqualTo(paymentId)
        verify { paymentRepository.save(payment) }
    }


    @Test
    fun `return success payment it for a single book chapter payment`() {
        val book = BookMother().inProgressBook(bookId, authorId)
        val chapter = ChapterMother().firstOne(authorId, bookId)
        val payment = singleChapterPayment()
        every { bookRepository.findById(BookId(bookId)) } returns book
        every { chapterRepository.findById(ChapterId(chapterId)) } returns chapter
        every { paymentProvider.start(PaymentAttempt.of(ReaderId(readerId), book, chapter)) } returns payment

        val response = service.execute(CreatePaymentRequest(readerId, bookId, chapterId))

        response as PaymentCreatedResponse
        assertThat(response.id).isEqualTo(paymentId)
        verify { paymentRepository.save(payment) }
    }

    private fun wholeBookPayment() =
        BookPayment(PaymentId(paymentId), ReaderId(readerId), Status.PENDING, Money(2.5f, Currency.EUR), BookId(bookId))

    private fun singleChapterPayment() =
        ChapterPayment(
            PaymentId(paymentId), ReaderId(readerId), Status.PENDING, Money(2.5f, Currency.EUR), BookId(bookId),
            ChapterId(chapterId)
        )

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val anotherBookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
        private val readerId = UUID.randomUUID().toString()
        private val paymentId = "any-payment-id"
    }
}