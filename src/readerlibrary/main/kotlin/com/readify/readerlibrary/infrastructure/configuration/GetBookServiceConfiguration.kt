package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.getbook.GetBookService
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetBookServiceConfiguration {
    @Bean
    fun getBookService(bookRepository: BookRepository, chapterRepository: ChapterRepository) =
        GetBookService(bookRepository, chapterRepository)
}