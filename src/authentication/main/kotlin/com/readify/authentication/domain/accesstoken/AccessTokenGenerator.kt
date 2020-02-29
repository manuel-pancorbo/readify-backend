package com.readify.authentication.domain.accesstoken

import com.readify.authentication.domain.usercredentials.UserCredentials

interface AccessTokenGenerator {
    fun generate(userCredentials: UserCredentials): String
}
