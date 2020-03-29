package com.readify.shared.domain.event.book

import com.readify.shared.domain.event.DomainEvent
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class ChapterUpdated(
    val bookId: String,
    val id: String,
    val authorId: String,
    val title: String,
    val modifiedAt: ZonedDateTime,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)
