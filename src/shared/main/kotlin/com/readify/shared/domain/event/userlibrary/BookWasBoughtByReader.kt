package com.readify.shared.domain.event.userlibrary

import com.readify.shared.domain.event.DomainEvent
import java.time.LocalDateTime

data class BookWasBoughtByReader(
    val readerId: String,
    val bookId: String,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)