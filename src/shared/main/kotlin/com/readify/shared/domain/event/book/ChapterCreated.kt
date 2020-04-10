package com.readify.shared.domain.event.book

import com.readify.shared.domain.event.DomainEvent
import com.readify.shared.domain.money.Money
import java.time.LocalDateTime

data class ChapterCreated(
    val id: String,
    val title: String,
    val content: String,
    val authorId: String,
    val bookId: String,
    val order: Int,
    val excerpt: String?,
    val price: Money,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)
