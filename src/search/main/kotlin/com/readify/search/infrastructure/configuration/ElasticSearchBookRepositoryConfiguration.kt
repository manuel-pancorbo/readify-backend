package com.readify.search.infrastructure.configuration

import com.readify.search.infrastructure.domain.book.ElasticSearchBookRepository
import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticSearchBookRepositoryConfiguration {

    @Value("\${elasticsearch.host}")
    private lateinit var host: String

    @Value("\${elasticsearch.port}")
    private lateinit var port: String

    @Bean
    fun elasticSearchBookRepository(jestClient: JestClient) = ElasticSearchBookRepository(jestClient)

    @Bean
    fun getUnsignedJestClient(): JestClient? {
        val factory = JestClientFactory()
        factory.setHttpClientConfig(
            HttpClientConfig.Builder(String.format("%s:%s", host, port))
                .multiThreaded(true)
                .build()
        )

        return factory.getObject()
    }
}
