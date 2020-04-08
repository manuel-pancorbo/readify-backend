package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.getchapterservice.GetChapterService
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetChapterServiceConfiguration {
    @Bean
    fun getChapterService(chapterRepository: ChapterRepository) = GetChapterService(chapterRepository)
}