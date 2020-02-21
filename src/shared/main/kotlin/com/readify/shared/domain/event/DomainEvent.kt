package com.readify.shared.domain.event

import java.time.LocalDateTime

open class DomainEvent(open val occurredOn: LocalDateTime)