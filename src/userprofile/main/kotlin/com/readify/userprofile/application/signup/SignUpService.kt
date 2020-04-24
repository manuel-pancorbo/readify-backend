package com.readify.userprofile.application.signup

import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.FullName
import com.readify.userprofile.domain.user.Image
import com.readify.userprofile.domain.user.UserFactory
import com.readify.userprofile.domain.user.UserRepository
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.user.Website
import com.readify.userprofile.domain.usercredentials.PlainPassword

class SignUpService(
    private val userFactory: UserFactory,
    private val userRepository: UserRepository
) {
    fun execute(request: SignUpRequest) =
        userFactory.create(Username(request.username), Email(request.email), FullName(request.fullName),
            request.image?.let { Image(it) }, request.website?.let { Website(it) }, PlainPassword(request.password))
            .also { userRepository.save(it) }
            .let { SignUpResponse }
}

class SignUpRequest(
    val username: String, val email: String, val password: String, val fullName: String,
    val image: String?, val website: String?
)

object SignUpResponse