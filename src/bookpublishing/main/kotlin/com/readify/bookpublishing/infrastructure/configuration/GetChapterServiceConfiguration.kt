package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.getchapterservice.GetChapterService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetChapterServiceConfiguration {
    @Bean
    fun getChapterService() = GetChapterService()
}