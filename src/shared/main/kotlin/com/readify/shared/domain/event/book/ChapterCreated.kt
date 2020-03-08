package com.readify.shared.domain.event.book

import com.readify.shared.domain.event.DomainEvent
import java.time.LocalDateTime

data class ChapterCreated(
    val id: String,
    val title: String,
    val authorId: String,
    val bookId: String,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)
