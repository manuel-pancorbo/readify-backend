package com.readify.search.infrastructure.eventsubscriber

import com.readify.search.application.service.adduser.AddAuthorRequest
import com.readify.search.application.service.adduser.AddAuthorService
import com.readify.shared.domain.event.user.UserCreated
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class UserSubscriber(private val addUserService: AddAuthorService) {
    @EventListener
    fun on(event: UserCreated) {
        addUserService.execute(AddAuthorRequest(event.userId, event.fullName, event.username))
    }
}