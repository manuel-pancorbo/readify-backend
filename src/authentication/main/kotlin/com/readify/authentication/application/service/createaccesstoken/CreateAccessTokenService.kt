package com.readify.authentication.application.service.createaccesstoken

import com.readify.authentication.domain.accesstoken.AccessTokenGenerator
import com.readify.authentication.domain.usercredentials.InvalidUserCredentialsException
import com.readify.authentication.domain.usercredentials.PasswordEncoderService
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.domain.usercredentials.UserIdentifier

class CreateAccessTokenService(
    private val userCredentialsRepository: UserCredentialsRepository,
    private val passwordEncoderService: PasswordEncoderService,
    private val accessTokenGenerator: AccessTokenGenerator
) {
    fun execute(request: CreateAccessTokenRequest): CreateAccessTokenResponse =
        userCredentialsRepository
            .findByUserIdentifier(UserIdentifier(request.userIdentifier))
            ?.takeIf { passwordEncoderService.match(request.plainPassword, it.encodedPassword) }
            ?.let {CreateAccessTokenResponse(accessTokenGenerator.generate(it))  }
            ?: throw InvalidUserCredentialsException()

}

data class CreateAccessTokenRequest(val userIdentifier: String, val plainPassword: String)
data class CreateAccessTokenResponse(val token: String)
