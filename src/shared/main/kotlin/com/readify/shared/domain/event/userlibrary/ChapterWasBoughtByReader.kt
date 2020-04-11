package com.readify.shared.domain.event.userlibrary

import com.readify.shared.domain.event.DomainEvent
import java.time.LocalDateTime

data class ChapterWasBoughtByReader(
    val readerId: String,
    val bookId: String,
    val chapterId: String,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)