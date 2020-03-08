package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.createchapter.CreateChapterService
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.Chapter
import com.readify.bookpublishing.domain.chapter.ChapterFactory
import com.readify.bookpublishing.domain.chapter.ChapterRepository
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
    fun chapterRepository() = object : ChapterRepository {
        override fun save(chapter: Chapter) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    fun chapterFactory(eventBus: EventBus) = ChapterFactory(eventBus)
}