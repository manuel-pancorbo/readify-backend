package com.readify.userprofile.domain.user

import com.readify.shared.domain.event.bus.EventBus
import com.readify.userprofile.domain.usercredentials.PlainPassword
import java.util.UUID

class UserFactory(private val userRepository: UserRepository, private val eventBus: EventBus) {
    fun create(
        username: Username,
        email: Email,
        plainPassword: PlainPassword
    ): User {
        userRepository.findByUsername(username.value)
            ?.let { throw UsernameAlreadyRegisteredException(username.value) }

        userRepository.findByEmail(email.value)
            ?.let { throw EmailAlreadyRegisteredException(email.value) }

        return User.create(newUserId(), username, email, plainPassword)
            .also { eventBus.publish(it.pullDomainEvents()) }
    }

    private fun newUserId() = UserId(UUID.randomUUID().toString())
}