package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.createbook.CreateBookService
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.infrastructure.domain.book.JpaBookRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaBookDataSource
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateBookServiceConfiguration {
    @Bean
    fun createBookService(bookRepository: BookRepository, bookFactory: BookFactory) =
        CreateBookService(bookFactory, bookRepository)

    @Bean
    fun bookFactory(eventBus: EventBus) = BookFactory(eventBus)

    @Bean
    fun bookRepository(jpaBookDataSource: JpaBookDataSource) = JpaBookRepository(jpaBookDataSource)
}