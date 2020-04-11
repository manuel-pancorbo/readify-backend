package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.infrastructure.domain.book.JpaBookRepository
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaBookDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReaderLibraryBookRepositoryConfiguration {
    @Bean
    fun readerLibraryBookRepository(jpaBookDataSource: ReaderLibraryJpaBookDataSource) =
        JpaBookRepository(jpaBookDataSource)
}