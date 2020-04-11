package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.addbooktoreaderlibrary.AddBookToReaderLibraryService
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AddBookToReaderLibraryServiceConfiguration {
    @Bean
    fun addBookToReaderLibraryService(repository: ReaderLibraryRepository, eventBus: EventBus) =
        AddBookToReaderLibraryService(repository, eventBus)
}