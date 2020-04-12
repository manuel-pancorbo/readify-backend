package com.readify.search.infrastructure.configuration

import com.readify.search.infrastructure.domain.book.ElasticSearchBookRepository
import io.searchbox.client.JestClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticSearchBookRepositoryConfiguration {
    @Bean
    fun elasticSearchBookRepository(jestClient: JestClient) = ElasticSearchBookRepository(jestClient)
}
