package com.readify.authentication.infrastructure.configuration

import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.authentication.infrastructure.springboot.HeaderRequesterArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ArgumentResolverConfiguration(private val tokenVerifierService: VerifyAccessTokenService) : WebMvcConfigurer {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(HeaderRequesterArgumentResolver(tokenVerifierService))
    }
}