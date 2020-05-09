package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.getreaderbookchapter.GetReaderBookChapterService
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetReaderBookChapterServiceConfiguration {
    @Bean
    fun getReaderBookChapterService(
        readerLibraryRepository: ReaderLibraryRepository,
        chapterRepository: ChapterRepository
    ) = GetReaderBookChapterService(readerLibraryRepository, chapterRepository)
}