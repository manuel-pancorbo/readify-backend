package com.readify.shared.domain.event.book

import com.readify.shared.domain.book.Status
import com.readify.shared.domain.book.Visibility
import com.readify.shared.domain.event.DomainEvent
import com.readify.shared.domain.money.Money
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class BookUpdated(
    val id: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    val tags: List<String>,
    val price: Money,
    val completionPercentage: Int,
    val visibility: Visibility,
    val status: Status,
    val finishedAt: ZonedDateTime? = null,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)