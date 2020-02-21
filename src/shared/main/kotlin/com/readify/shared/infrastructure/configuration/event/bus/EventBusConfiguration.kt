package com.readify.shared.infrastructure.configuration.event.bus

import com.readify.shared.infrastructure.domain.event.SpringEventBus
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventBusConfiguration {
    @Bean
    fun springEventBus(publisher: ApplicationEventPublisher) =
        SpringEventBus(publisher)
}