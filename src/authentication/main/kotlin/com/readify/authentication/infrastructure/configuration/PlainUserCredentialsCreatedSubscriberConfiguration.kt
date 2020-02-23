package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsService
import com.readify.authentication.domain.usercredentials.EncryptedPassword
import com.readify.authentication.domain.usercredentials.PasswordEncrypterService
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateUserCredentialsConfiguration {
    @Bean
    fun service() = CreateUserCredentialsService(object : PasswordEncrypterService {
        override fun encrypt(plainPassword: String): EncryptedPassword {
            return EncryptedPassword("test")
        }

    }, object : UserCredentialsRepository {
        override fun save(userCredentials: UserCredentials) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    })
}