package com.readify.authentication.infrastructure.domain.usercredentials

import com.readify.authentication.domain.usercredentials.EncryptedPassword
import com.readify.authentication.domain.usercredentials.PasswordEncrypterService
import org.springframework.security.crypto.bcrypt.BCrypt

private const val SALT_ITERATIONS = 11

class BcryptPasswordEncrypterService : PasswordEncrypterService {
    override fun encrypt(plainPassword: String) =
        EncryptedPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_ITERATIONS)))

    override fun match(plainPassword: String, encryptedPassword: EncryptedPassword) =
        BCrypt.checkpw(plainPassword, encryptedPassword.value)
}