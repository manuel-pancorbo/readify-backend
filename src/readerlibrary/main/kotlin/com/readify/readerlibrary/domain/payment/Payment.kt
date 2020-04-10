package com.readify.readerlibrary.domain.payment

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime
import java.util.UUID

sealed class Payment(
    open val id: PaymentId, open val readerId: ReaderId, open val status: Status, open val amount: Money,
    open val startedAt: ZonedDateTime, open val completedAt: ZonedDateTime?
) : RootAggregate()

data class BookPayment(
    override val id: PaymentId, override val readerId: ReaderId, override val status: Status,
    override val amount: Money, val bookId: BookId, override val startedAt: ZonedDateTime = Clock().now(),
    override val completedAt: ZonedDateTime? = null
) : Payment(id, readerId, status, amount, startedAt, completedAt)

data class ChapterPayment(
    override val id: PaymentId, override val readerId: ReaderId, override val status: Status,
    override val amount: Money, val bookId: BookId, val chapterId: ChapterId,
    override val startedAt: ZonedDateTime = Clock().now(), override val completedAt: ZonedDateTime? = null
) : Payment(id, readerId, status, amount, startedAt, completedAt)

enum class Status { PENDING, COMPLETED }
data class ReaderId(val value: String) {
    init {
        UUID.fromString(value)
    }
}