package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenService
import com.readify.authentication.domain.accesstoken.AccessTokenGenerator
import com.readify.authentication.domain.usercredentials.PasswordEncoderService
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.infrastructure.domain.accesstoken.JwtAccessTokenGenerator
import com.readify.shared.domain.clock.Clock
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateAccessTokenServiceConfiguration {
    @Bean
    fun createAccessTokenService(
        userCredentialsRepository: UserCredentialsRepository,
        passwordEncoderService: PasswordEncoderService,
        accessTokenGenerator: AccessTokenGenerator
    ) =
        CreateAccessTokenService(userCredentialsRepository, passwordEncoderService, accessTokenGenerator)

    @Bean
    fun jwtAccessTokenGenerator(@Value("\${authentication.hmac256.secret}") secret: String) =
        JwtAccessTokenGenerator(secret, Clock())
}