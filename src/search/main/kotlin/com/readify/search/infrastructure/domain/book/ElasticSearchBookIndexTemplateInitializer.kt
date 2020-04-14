package com.readify.search.infrastructure.domain.book

import io.searchbox.client.JestClient
import io.searchbox.indices.template.PutTemplate
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Scanner
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@Profile("!contract-test")
@Component
class ElasticSearchBookIndexTemplateInitializer(val client: JestClient) {
    @PostConstruct
    @Throws(IOException::class)
    fun putIndexTemplate() {
        val msg: String = getContentFrom(TEMPLATE_PATH)
        val result = client.execute(
            PutTemplate.Builder(TEMPLATE_NAME, msg)
                .build()
        )
        if (!result.isSucceeded) {
            throw IOException(result.errorMessage)
        }
        logger.info("[index_template_updated] {} updated in ElasticSearch.", TEMPLATE_PATH)
    }

    fun getContentFrom(resourcePath: String): String =
        this::class.java.getResourceAsStream(resourcePath)
            .let { Scanner(it, StandardCharsets.UTF_8.name()).useDelimiter("\\A") }
            .takeIf { it.hasNext() }
            ?.next()
            ?: throw IllegalAccessError()

    companion object {
        private const val TEMPLATE_NAME = "book-index-template"
        private const val TEMPLATE_PATH = "/elasticsearch/index-template.json"
    }
}