package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksService
import com.readify.bookpublishing.domain.book.BookRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetAuthorBooksServiceConfiguration {
    @Bean
    fun getAuthorBooksService(bookRepository: BookRepository) = GetAuthorBooksService(bookRepository)
}