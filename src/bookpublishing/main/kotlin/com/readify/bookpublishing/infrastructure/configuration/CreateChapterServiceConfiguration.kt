package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.createchapter.CreateChapterService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateChapterServiceConfiguration {
    @Bean
    fun createChapterService() = CreateChapterService()
}