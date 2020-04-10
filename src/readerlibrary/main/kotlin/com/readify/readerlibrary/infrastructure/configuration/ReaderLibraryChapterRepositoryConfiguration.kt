package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.infrastructure.domain.chapter.JpaChapterRepository
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaChapterDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReaderLibraryChapterRepositoryConfiguration {
    @Bean
    fun readerLibraryChapterRepository(jpaChapterDataSource: ReaderLibraryJpaChapterDataSource) =
        JpaChapterRepository(jpaChapterDataSource)
}
