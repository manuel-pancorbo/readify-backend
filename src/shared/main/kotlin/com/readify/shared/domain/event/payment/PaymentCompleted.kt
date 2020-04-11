package com.readify.shared.domain.event.payment

import com.readify.shared.domain.event.DomainEvent
import com.readify.shared.domain.money.Money
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class PaymentCompleted(
    val id: String,
    val readerId: String,
    val amount: Money,
    val bookId: String,
    val chapterId: String?,
    val startedAt: ZonedDateTime,
    val completedAt: ZonedDateTime,
    val type: PaymentType,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)

enum class PaymentType { BOOK, CHAPTER }