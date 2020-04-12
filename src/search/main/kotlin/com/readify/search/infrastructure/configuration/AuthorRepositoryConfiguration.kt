package com.readify.search.infrastructure.configuration

import com.readify.search.infrastructure.domain.author.JpaAuthorRepository
import com.readify.search.infrastructure.jpa.search.SearchJpaAuthorDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthorRepositoryConfiguration {
    @Bean
    fun searchAuthorRepository(dataSource: SearchJpaAuthorDataSource) =
        JpaAuthorRepository(dataSource)
}