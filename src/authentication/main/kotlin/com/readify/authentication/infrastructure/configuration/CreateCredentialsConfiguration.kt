package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsService
import com.readify.authentication.domain.usercredentials.PasswordEncrypterService
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.infrastructure.domain.usercredentials.BcryptPasswordEncrypterService
import com.readify.authentication.infrastructure.jpa.usercredentials.JpaUserCredentialsDataSource
import com.readify.authentication.infrastructure.jpa.usercredentials.JpaUserCredentialsRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateUserCredentialsConfiguration {
    @Bean
    fun service(
        passwordEncrypterService: PasswordEncrypterService,
        userCredentialsRepository: UserCredentialsRepository
    ) =
        CreateUserCredentialsService(passwordEncrypterService, userCredentialsRepository)

    @Bean
    fun passwordEncrypterService() = BcryptPasswordEncrypterService()

    @Bean
    fun userCredentialsRepository(jpaUserCredentialsDataSource: JpaUserCredentialsDataSource) =
        JpaUserCredentialsRepository(jpaUserCredentialsDataSource)
}