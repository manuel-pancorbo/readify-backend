package com.readify.authentication.infrastructure.eventsubscriber

import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsRequest
import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsService
import com.readify.shared.domain.event.user.PlainUserCredentialsCreated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class PlainUserCredentialsCreatedSubscriber(private val applicationService: CreateUserCredentialsService) {
    @EventListener
    fun on(event: PlainUserCredentialsCreated) {
        applicationService.execute(
            CreateUserCredentialsRequest(
                event.userId,
                event.username,
                event.email,
                event.plainPassword
            )
        )
    }
}