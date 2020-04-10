package com.readify.shared.domain.event.book

import com.readify.shared.domain.chapter.Status
import com.readify.shared.domain.event.DomainEvent
import com.readify.shared.domain.money.Money
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class ChapterUpdated(
    val bookId: String,
    val id: String,
    val authorId: String,
    val title: String,
    val content: String,
    val modifiedAt: ZonedDateTime,
    val order: Int,
    val excerpt: String?,
    val price: Money,
    val status: Status,
    val publishedAt: ZonedDateTime?,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)
