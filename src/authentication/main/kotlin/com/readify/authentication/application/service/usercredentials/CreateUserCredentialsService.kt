package com.readify.authentication.application.service.usercredentials

import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.PasswordEncoderService
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.Username

class CreateUserCredentialsService(
    private val passwordEncoderService: PasswordEncoderService,
    private val userCredentialsRepository: UserCredentialsRepository
) {
    fun execute(request: CreateUserCredentialsRequest) {
        request.toDomain(passwordEncoderService)
            .let { userCredentialsRepository.save(it) }
    }

}

private fun CreateUserCredentialsRequest.toDomain(passwordEncoderService: PasswordEncoderService) =
    UserCredentials(
        UserId(this.userId),
        Username(this.username),
        Email(this.email),
        passwordEncoderService.encode(this.plainPassword)
    )

data class CreateUserCredentialsRequest(
    val userId: String,
    val username: String,
    val email: String,
    val plainPassword: String
)
