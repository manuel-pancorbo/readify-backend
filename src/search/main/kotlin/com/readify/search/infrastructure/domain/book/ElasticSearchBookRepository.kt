package com.readify.search.infrastructure.domain.book

import com.readify.search.domain.book.Book
import com.readify.search.domain.book.BookRepository
import com.readify.shared.domain.clock.Clock
import io.searchbox.client.JestClient
import io.searchbox.core.Index
import java.time.format.DateTimeFormatter

class ElasticSearchBookRepository(
    private val client: JestClient
) : BookRepository {
    override fun save(book: Book) {
        client.execute(
            Index.Builder(book.toElasticSearchBook())
                .refresh(true)
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .id(book.bookId.value)
                .build()
        )
    }

    companion object {
        private const val INDEX_NAME = "books"
        private const val TYPE_NAME = "_doc"
    }
}

private fun Book.toElasticSearchBook() =
    ElasticSearchBook(
        bookId.value, authorId.value, title.value, cover.value, summary.value, tags.value,
        ElasticSearchMoney(price.amount, price.currency.toString().toLowerCase()), completionPercentage.value,
        status.toString().toLowerCase(),
        finishedAt?.let { Clock().toUtc(it).format(DateTimeFormatter.ISO_ZONED_DATE_TIME) }
    )
