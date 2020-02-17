package com.readify.userprofile.domain.user

import java.util.UUID

class UserFactory(private val userRepository: UserRepository) {
    fun create(username: Username, email: Email): User {
        userRepository.findByUsername(username.value)
            ?.let { throw UsernameAlreadyRegisteredException(username.value) }

        userRepository.findByEmail(email.value)
            ?.let { throw EmailAlreadyRegisteredException(email.value) }

        return User(newUserId(), username, email)
    }

    private fun newUserId() = UserId(UUID.randomUUID().toString())
}