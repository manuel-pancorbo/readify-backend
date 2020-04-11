package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.infrastructure.domain.readerlibrary.JpaReaderLibraryRepository
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaLibraryBookDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReaderLibraryRepositoryConfiguration {
    @Bean
    fun readerLibraryRepository(dataSource: ReaderLibraryJpaLibraryBookDataSource) =
        JpaReaderLibraryRepository(dataSource)
}