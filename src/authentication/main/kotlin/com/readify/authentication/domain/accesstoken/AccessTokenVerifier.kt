package com.readify.authentication.domain.accesstoken

interface AccessTokenVerifier {
    fun verify(accessToken: String): UserInformation
}