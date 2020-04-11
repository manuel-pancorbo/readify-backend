package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.addchaptertoreaderlibrary.AddChapterToReaderLibraryService
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AddChapterToReaderLibraryServiceConfiguration {
    @Bean
    fun addChapterToReaderLibraryService(repository: ReaderLibraryRepository, eventBus: EventBus) =
        AddChapterToReaderLibraryService(repository, eventBus)
}