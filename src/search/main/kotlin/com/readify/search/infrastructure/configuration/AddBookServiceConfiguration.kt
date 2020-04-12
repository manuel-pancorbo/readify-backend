package com.readify.search.infrastructure.configuration

import com.readify.search.application.service.addbook.AddBookService
import com.readify.search.domain.book.BookRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AddBookServiceConfiguration {
    @Bean
    fun addBookService(bookRepository: BookRepository) = AddBookService(bookRepository)
}
