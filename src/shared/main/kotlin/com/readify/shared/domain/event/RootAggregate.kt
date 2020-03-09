package com.readify.shared.domain.event

abstract class RootAggregate {
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()

    fun pullDomainEvents(): List<DomainEvent> = domainEvents.toList()

    fun record(event: DomainEvent) {
        domainEvents.add(event)
    }
}