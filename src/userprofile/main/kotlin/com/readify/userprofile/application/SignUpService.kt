package com.readify.userprofile.application

import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserFactory
import com.readify.userprofile.domain.user.UserRepository
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.usercredentials.PlainPassword

class SignUpService(
    private val userFactory: UserFactory,
    private val userRepository: UserRepository
) {
    fun execute(request: SignUpRequest) =
        userFactory.create(Username(request.username), Email(request.email), PlainPassword(request.password))
            .also { userRepository.save(it) }
            .toResponse()
}

private fun User.toResponse() =
    SignUpResponse(this.id.value, this.username.value, this.email.value)

class SignUpRequest(val username: String, val email: String, val password: String)
class SignUpResponse(val id: String, val username: String, val email: String)