package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksService
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetReaderBooksServiceConfiguration {
    @Bean
    fun getReaderBooksService(libraryRepository: ReaderLibraryRepository, bookRepository: BookRepository) =
        GetReaderBooksService(libraryRepository, bookRepository)
}