package com.readify.userprofile.application

import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import com.readify.userprofile.domain.user.Password
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.user.UsernameAlreadyRegisteredException
import java.util.UUID

class SignUpService(private val userRepository: UserRepository) {
    fun execute(request: SignUpRequest): SignUpResponse {
        userRepository.findByUsername(request.username)
            ?.let { throw UsernameAlreadyRegisteredException(request.username) }

        userRepository.findByEmail(request.email)
            ?.let { throw EmailAlreadyRegisteredException(request.email) }

        return userRepository.save(request.toDomain()).toResponse()
    }
}

private fun SignUpRequest.toDomain() =
    User(UserId(UUID.randomUUID().toString()), Username(this.username), Email(this.email), Password(this.password))

private fun User.toResponse() =
    SignUpResponse(this.id.value, this.username.value, this.email.value)

class SignUpRequest(val username: String, val email: String, val password: String)
class SignUpResponse(val id: String, val username: String, val email: String)