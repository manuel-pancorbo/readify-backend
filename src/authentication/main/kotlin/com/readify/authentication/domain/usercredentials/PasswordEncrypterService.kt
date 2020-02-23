package com.readify.authentication.domain.usercredentials

interface PasswordEncrypterService {
    fun encrypt(plainPassword: String): EncryptedPassword
}
