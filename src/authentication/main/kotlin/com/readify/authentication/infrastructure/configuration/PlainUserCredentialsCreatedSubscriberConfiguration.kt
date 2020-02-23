package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateUserCredentialsConfiguration {
    @Bean
    fun service() = CreateUserCredentialsService()
}