package com.readify.search.infrastructure.configuration

import com.readify.search.application.service.searchbooks.SearchBooksService
import com.readify.search.domain.book.BookRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SearchBooksServiceConfiguration {
    @Bean
    fun searchBooksService(bookRepository: BookRepository) = SearchBooksService(bookRepository)
}
