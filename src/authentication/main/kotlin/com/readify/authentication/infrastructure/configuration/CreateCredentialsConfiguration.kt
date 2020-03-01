package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsService
import com.readify.authentication.domain.usercredentials.PasswordEncoderService
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.infrastructure.domain.usercredentials.BcryptPasswordEncoderService
import com.readify.authentication.infrastructure.jpa.usercredentials.JpaUserCredentialsDataSource
import com.readify.authentication.infrastructure.jpa.usercredentials.JpaUserCredentialsRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateUserCredentialsServiceConfiguration {
    @Bean
    fun createUserCredentialsService(
        passwordEncoderService: PasswordEncoderService,
        userCredentialsRepository: UserCredentialsRepository
    ) =
        CreateUserCredentialsService(passwordEncoderService, userCredentialsRepository)

    @Bean
    fun passwordEncrypterService() = BcryptPasswordEncoderService()

    @Bean
    fun userCredentialsRepository(jpaUserCredentialsDataSource: JpaUserCredentialsDataSource) =
        JpaUserCredentialsRepository(jpaUserCredentialsDataSource)
}