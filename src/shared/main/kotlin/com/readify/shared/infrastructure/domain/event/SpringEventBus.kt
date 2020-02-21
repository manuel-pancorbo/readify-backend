package com.readify.shared.infrastructure.domain.event

import com.readify.shared.domain.event.DomainEvent
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.ApplicationEventPublisher

class SpringEventBus(private val publisher: ApplicationEventPublisher): EventBus {
    override fun publish(events: List<DomainEvent>) {
        events.forEach { publisher.publishEvent(it) }
    }
}