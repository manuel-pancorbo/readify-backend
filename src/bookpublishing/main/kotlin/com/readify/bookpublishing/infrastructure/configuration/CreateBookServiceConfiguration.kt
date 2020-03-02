package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.createbook.CreateBookService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateBookServiceConfiguration {
    @Bean
    fun createBookService() = CreateBookService()
}