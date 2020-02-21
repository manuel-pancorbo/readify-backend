package com.readify.shared.domain.event.bus

import com.readify.shared.domain.event.DomainEvent

interface EventBus {
    fun publish(events: List<DomainEvent>)
}