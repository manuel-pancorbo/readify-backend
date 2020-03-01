package com.readify.authentication.application.service.verifyaccesstoken

import com.readify.authentication.domain.accesstoken.AccessTokenVerifier
import com.readify.authentication.domain.accesstoken.UserInformation

class VerifyAccessTokenService(private val tokenVerifier: AccessTokenVerifier) {
    fun execute(request: VerifyAccessTokenRequest) =
        tokenVerifier.verify(request.token).toResponse()

}

data class VerifyAccessTokenRequest(val token: String)
data class VerifyAccessTokenResponse(val userId: String, val username: String, val email: String)
private fun UserInformation.toResponse() = VerifyAccessTokenResponse(this.userId, this.username, this.email)
