package com.readify.authentication.domain.usercredentials

interface PasswordEncoderService {
    fun encode(plainPassword: String): EncodedPassword
    fun match(plainPassword: String, encodedPassword: EncodedPassword): Boolean
}
