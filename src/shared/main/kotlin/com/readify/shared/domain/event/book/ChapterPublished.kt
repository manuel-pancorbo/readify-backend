package com.readify.shared.domain.event.book

import com.readify.shared.domain.event.DomainEvent
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class ChapterPublished(
    val id: String,
    val publishedAt: ZonedDateTime,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)
