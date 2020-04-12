package com.readify.search.infrastructure.domain.book

import com.readify.search.domain.book.AuthorId
import com.readify.search.domain.book.Book
import com.readify.search.domain.book.BookId
import com.readify.search.domain.book.BookRepository
import com.readify.search.domain.book.BookSearchResult
import com.readify.search.domain.book.BookStatus
import com.readify.search.domain.book.CompletionPercentage
import com.readify.search.domain.book.Cover
import com.readify.search.domain.book.SearchCriteria
import com.readify.search.domain.book.Summary
import com.readify.search.domain.book.Tags
import com.readify.search.domain.book.Title
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import io.searchbox.client.JestClient
import io.searchbox.core.Index
import io.searchbox.core.Search
import mu.KotlinLogging
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.CROSS_FIELDS
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.PHRASE
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders.boolQuery
import org.elasticsearch.index.query.QueryBuilders.multiMatchQuery
import org.elasticsearch.index.query.QueryBuilders.termQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

class ElasticSearchBookRepository(private val client: JestClient) : BookRepository {
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

    override fun search(searchCriteria: SearchCriteria): BookSearchResult {
        val search = Search.Builder(SearchSourceBuilder().query(buildQuery(searchCriteria)).toString())
            .addIndex(INDEX_NAME)
            .addType(TYPE_NAME)
            .build()

        val searchResult = client.execute(search)

        return if (searchResult.isSucceeded) {
            BookSearchResult(
                searchResult.total.toInt(),
                searchResult.getHits(ElasticSearchBook::class.java).map { it.source.toDomain() }
            )
        } else {
            logger.error { "ElasticSearch query error: ${searchResult.errorMessage}" }
            BookSearchResult.empty()
        }
    }

    private fun buildQuery(searchCriteria: SearchCriteria) =
        boolQuery().also {
            searchCriteria.tagFilter?.let { tag -> it.filter(termQuery("tags", tag.value)) }
            searchCriteria.authorFilter?.let { author -> it.filter(termQuery("authorId", author.value)) }
            searchCriteria.textFilter?.let { text ->
                it.must(
                    multiMatchQuery(text.value)
                        .field("title")
                        .field("summary")
                        .type(CROSS_FIELDS)
                        .operator(Operator.OR)
                )

                it.should(
                    multiMatchQuery(text.value)
                        .field("title")
                        .field("summary")
                        .type(PHRASE)
                        .operator(Operator.OR)
                )
            }
        }

    companion object {
        private const val INDEX_NAME = "books"
        private const val TYPE_NAME = "_doc"
    }
}

private fun ElasticSearchBook.toDomain() =
    Book(BookId(bookId), AuthorId(authorId), Title(title), Cover(cover), Summary(summary), Tags(tags),
        Money(price.amount, Currency.valueOf(price.currency)), CompletionPercentage(completionPercentage),
        BookStatus.valueOf(status), finishedAt?.let { Clock().fromUtc(ZonedDateTime.parse(it)) }
    )

private fun Book.toElasticSearchBook() =
    ElasticSearchBook(
        bookId.value, authorId.value, title.value, cover.value, summary.value, tags.value,
        ElasticSearchMoney(price.amount, price.currency.toString()), completionPercentage.value,
        status.toString(), finishedAt?.let { Clock().toUtc(it).format(DateTimeFormatter.ISO_ZONED_DATE_TIME) }
    )
