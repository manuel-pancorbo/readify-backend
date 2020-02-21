package com.readify.shared.domain.event.user

import com.readify.shared.domain.event.DomainEvent
import java.time.LocalDateTime

data class UserCreated(
    val userId: String,
    val username: String,
    val email: String,
    override val occurredOn: LocalDateTime = LocalDateTime.now()
) : DomainEvent(occurredOn)