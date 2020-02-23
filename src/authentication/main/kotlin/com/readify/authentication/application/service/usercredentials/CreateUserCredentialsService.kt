package com.readify.authentication.application.service.usercredentials

import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.PasswordEncrypterService
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.Username

class CreateUserCredentialsService(
    private val passwordEncrypterService: PasswordEncrypterService,
    private val userCredentialsRepository: UserCredentialsRepository
) {
    fun execute(request: CreateUserCredentialsRequest) {
        request.toDomain(passwordEncrypterService)
            .let { userCredentialsRepository.save(it) }
    }

}

private fun CreateUserCredentialsRequest.toDomain(passwordEncrypterService: PasswordEncrypterService) =
    UserCredentials(
        UserId(this.userId),
        Username(this.username),
        Email(this.email),
        passwordEncrypterService.encrypt(this.plainPassword)
    )

data class CreateUserCredentialsRequest(
    val userId: String,
    val username: String,
    val email: String,
    val plainPassword: String
)
