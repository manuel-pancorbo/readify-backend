package com.readify.readerlibrary.infrastructure.domain.payment

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.readify.IntegrationTest
import com.readify.bookpublishing.infrastructure.jpa.chapter.PaymentMother
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentStatus
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentType
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaPaymentDataSource
import com.readify.shared.domain.clock.Clock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class JpaPaymentRepositoryShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: JpaPaymentRepository

    @Autowired
    private lateinit var dataSource: ReaderLibraryJpaPaymentDataSource

    @BeforeEach
    fun setUp() {
        dataSource.deleteAll()
    }

    @Test
    fun `save a payment`() {
        val payment = PaymentMother.pendingBookPayment("any-payment-id")

        repository.save(payment)

        val actual = dataSource.findById(payment.id.value)
        assertThat(actual.isPresent).isTrue()
        assertThat(actual.get().amount).isEqualTo(payment.amount.amount)
        assertThat(actual.get().currency).isEqualTo(payment.amount.currency.toString())
        assertThat(actual.get().id).isEqualTo(payment.id.value)
        assertThat(actual.get().readerId).isEqualTo(payment.readerId.value)
        assertThat(actual.get().bookId).isEqualTo(payment.bookId.value)
        assertThat(actual.get().chapterId).isNull()
        assertThat(actual.get().completedAt).isNull()
        assertThat(actual.get().startedAt).isEqualTo(payment.startedAt.toInstant())
        assertThat(actual.get().type).isEqualTo(JpaPaymentType.BOOK)
        assertThat(actual.get().status).isEqualTo(JpaPaymentStatus.PENDING)
    }

    @Test
    fun `find by id`() {
        val payment = PaymentMother.pendingBookPayment("any-payment-id")
        repository.save(payment)

        val actual = repository.findById(payment.id)

        assertThat(actual).isEqualTo(payment)
    }

    @Test
    fun `find all payments completed by reader id sorted by completed date`() {
        val pendingPayment = PaymentMother.pendingBookPayment("any-pending-payment-id", readerId = readerId)
        val oldCompletedPayment =
            PaymentMother.completedChapterPayment("any-old-completed-payment-id", Clock().now().minusMonths(2), readerId)
        val newCompletedPayment =
            PaymentMother.completedChapterPayment("any-new-completed-payment-id", Clock().now(), readerId)
        val completedPaymentOfAnotherReader =
            PaymentMother.completedChapterPayment("any-new-completed-of-another-user-payment-id", Clock().now(), otherReaderId)
        repository.save(oldCompletedPayment)
        repository.save(newCompletedPayment)
        repository.save(completedPaymentOfAnotherReader)
        repository.save(pendingPayment)

        val payments = repository.findCompletedByReaderId(ReaderId(readerId))

        assertThat(payments).hasSize(2)
        assertThat(payments[0].id.value).isEqualTo("any-new-completed-payment-id")
        assertThat(payments[1].id.value).isEqualTo("any-old-completed-payment-id")
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val otherReaderId = UUID.randomUUID().toString()
    }
}