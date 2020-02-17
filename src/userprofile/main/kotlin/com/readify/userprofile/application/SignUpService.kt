package com.readify.userprofile.application

import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserFactory
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.usercredentials.PlainPassword
import com.readify.userprofile.domain.usercredentials.UserCredentials
import java.util.UUID

class SignUpService(private val userFactory: UserFactory, private val userRepository: UserRepository) {
    fun execute(request: SignUpRequest): SignUpResponse {
        val user = userFactory.create(Username(request.username), Email(request.email))
        val userCredentials = request.toUserCredentials()

        userRepository.save(user)

        return user.toResponse()
    }
}

private fun SignUpRequest.toUserCredentials() =
    UserCredentials(
        UserId(UUID.randomUUID().toString()),
        Email(this.email),
        Username(this.username),
        PlainPassword(this.password)
    )

private fun User.toResponse() =
    SignUpResponse(this.id.value, this.username.value, this.email.value)

class SignUpRequest(val username: String, val email: String, val password: String)
class SignUpResponse(val id: String, val username: String, val email: String)