package com.readify.readerlibrary.infrastructure.domain.payment

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.readify.IntegrationTest
import com.readify.bookpublishing.infrastructure.jpa.chapter.PaymentMother
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentStatus
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentType
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaPaymentDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

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
}