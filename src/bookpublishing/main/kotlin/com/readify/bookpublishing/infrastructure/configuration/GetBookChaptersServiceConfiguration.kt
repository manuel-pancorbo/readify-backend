package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersService
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.chapter.ChapterRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetBookChaptersServiceConfiguration {
    @Bean
    fun getBookChaptersService(bookRepository: BookRepository, chapterRepository: ChapterRepository) =
        GetBookChaptersService(bookRepository, chapterRepository)
}