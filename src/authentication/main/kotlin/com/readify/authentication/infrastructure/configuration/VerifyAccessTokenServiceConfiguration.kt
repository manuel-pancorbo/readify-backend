package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.authentication.domain.accesstoken.AccessTokenVerifier
import com.readify.authentication.infrastructure.domain.accesstoken.JwtAccessTokenVerifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VerifyAccessTokenServiceConfiguration {
    @Bean
    fun verifyAccessTokenService(tokenVerifier: AccessTokenVerifier) =
        VerifyAccessTokenService(tokenVerifier)

    @Bean
    fun tokenVerifier(@Value("\${authentication.hmac256.secret}") secret: String) =
        JwtAccessTokenVerifier(secret)
}