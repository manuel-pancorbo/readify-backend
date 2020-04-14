package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksService
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetReaderBookServiceConfiguration {
    @Bean
    fun getReaderBooksService(repository: ReaderLibraryRepository) = GetReaderBooksService(repository)
}