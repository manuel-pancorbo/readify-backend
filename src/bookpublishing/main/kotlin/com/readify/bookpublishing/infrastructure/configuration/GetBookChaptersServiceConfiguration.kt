package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetBookChaptersServiceConfiguration {
    @Bean
    fun getBookChaptersService() = GetBookChaptersService()
}