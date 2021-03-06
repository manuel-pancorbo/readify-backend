package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.getreaderbook.GetReaderBookService
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetReaderBookServiceConfiguration {
    @Bean
    fun getReaderBookService(repository: ReaderLibraryRepository) = GetReaderBookService(repository)
}