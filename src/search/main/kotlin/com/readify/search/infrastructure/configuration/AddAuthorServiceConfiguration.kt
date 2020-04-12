package com.readify.search.infrastructure.configuration

import com.readify.search.application.service.adduser.AddAuthorService
import com.readify.search.infrastructure.domain.author.JpaAuthorRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AddAuthorServiceConfiguration {
    @Bean
    fun addAuthorService(repository: JpaAuthorRepository) =
        AddAuthorService(repository)
}