package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.createchapter.CreateChapterService
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.ChapterFactory
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import com.readify.bookpublishing.infrastructure.domain.book.JpaChapterRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterDataSource
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateChapterServiceConfiguration {
    @Bean
    fun createChapterService(
        bookRepository: BookRepository,
        chapterRepository: ChapterRepository,
        chapterFactory: ChapterFactory
    ) =
        CreateChapterService(bookRepository, chapterRepository, chapterFactory)

    @Bean
    fun chapterRepository(jpaChapterDataSource: JpaChapterDataSource) =
        JpaChapterRepository(jpaChapterDataSource)

    @Bean
    fun chapterFactory(eventBus: EventBus) = ChapterFactory(eventBus)
}
