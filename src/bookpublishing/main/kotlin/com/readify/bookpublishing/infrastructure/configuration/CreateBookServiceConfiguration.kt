package com.readify.bookpublishing.infrastructure.configuration

import com.readify.bookpublishing.application.service.createbook.PublishBookService
import com.readify.bookpublishing.domain.book.Book
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.shared.domain.event.bus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateBookServiceConfiguration {
    @Bean
    fun createBookService(bookRepository: BookRepository, bookFactory: BookFactory) =
        PublishBookService(bookFactory, bookRepository)

    @Bean
    fun bookFactory(eventBus: EventBus) = BookFactory(eventBus)

    @Bean
    fun bookRepository() = object : BookRepository {
        override fun save(book: Book) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}