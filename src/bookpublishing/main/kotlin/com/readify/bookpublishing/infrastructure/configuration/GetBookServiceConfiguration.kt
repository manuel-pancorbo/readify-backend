package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.getbook.GetAuthorBookService
import com.readify.bookpublishing.domain.book.BookRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetBookServiceConfiguration {
    @Bean
    fun getBookService(bookRepository: BookRepository) = GetAuthorBookService(bookRepository)
}