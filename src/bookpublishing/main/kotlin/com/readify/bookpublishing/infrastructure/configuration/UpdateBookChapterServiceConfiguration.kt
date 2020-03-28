package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.updatechapter.UpdateBookChapterService
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdateBookChapterServiceConfiguration {
    @Bean
    fun updateBookChapterService(chapterRepository: ChapterRepository, eventBus: EventBus) =
        UpdateBookChapterService(chapterRepository, eventBus)
}