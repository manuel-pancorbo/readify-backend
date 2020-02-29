package com.readify.authentication.infrastructure.domain.usercredentials

import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.PasswordEncoderService
import org.springframework.security.crypto.bcrypt.BCrypt

private const val SALT_ITERATIONS = 11

class BcryptPasswordEncoderService : PasswordEncoderService {
    override fun encode(plainPassword: String) =
        EncodedPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_ITERATIONS)))

    override fun match(plainPassword: String, encodedPassword: EncodedPassword) =
        BCrypt.checkpw(plainPassword, encodedPassword.value)
}