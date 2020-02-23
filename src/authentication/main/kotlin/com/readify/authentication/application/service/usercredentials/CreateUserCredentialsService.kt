package com.readify.authentication.application.service.usercredentials

class CreateUserCredentialsService {
    fun execute(request: CreateUserCredentialsRequest) {

    }

}

data class CreateUserCredentialsRequest(
    val userId: String,
    val username: String,
    val email: String,
    val plainPassword: String
)
