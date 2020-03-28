package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.updatebook.UpdateBookService
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdateBookServiceConfiguration {
    @Bean
    fun updateBookService(bookRepository: BookRepository, eventBus: EventBus) =
        UpdateBookService(bookRepository, eventBus)
}