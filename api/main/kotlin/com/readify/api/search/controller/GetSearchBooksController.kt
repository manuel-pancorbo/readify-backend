package com.readify.api.search.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.search.application.service.searchbooks.SearchBooksRequest
import com.readify.search.application.service.searchbooks.SearchBooksResponse
import com.readify.search.application.service.searchbooks.SearchBooksService
import com.readify.search.application.service.common.ApplicationBook
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/v1")
class GetSearchBooksController(private val service: SearchBooksService) {
    @GetMapping("/books")
    fun getBook(
        @RequestParam(name = "text") text: String?,
        @RequestParam(name = "tag") tag: String?,
        @RequestParam(name = "author") author: String?
    ): ResponseEntity<out Any> =
        service.execute(
            SearchBooksRequest(
                text,
                tag,
                author
            )
        ).toHttpResponse()
}

private fun SearchBooksResponse.toHttpResponse() =
    ResponseEntity.ok(GetSearchBooksHttpResponse(total, results.map { it.toHttpResponse() }))

private fun ApplicationBook.toHttpResponse(): SearchBookHttpResponse {
    return SearchBookHttpResponse(bookId, authorId, title, summary, cover, tags, HttpMoney(priceAmount, priceCurrency),
    status.toString().toLowerCase(), completionPercentage, finishedAt)
}

data class GetSearchBooksHttpResponse(
    @JsonProperty("total") val total: Int,
    @JsonProperty("results") val results: List<SearchBookHttpResponse>
)
data class SearchBookHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("authorId") val authorId: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("summary") val summary: String,
    @JsonProperty("cover") val cover: String,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("price") val price: HttpMoney,
    @JsonProperty("status") val status: String,
    @JsonProperty("completionPercentage") val completionPercentage: Int,
    @JsonProperty("finishedAt") val finishedAt: ZonedDateTime?
)