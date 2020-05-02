package com.readify.api.readerlibrary.controller.getbook

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.readerlibrary.application.service.getbook.BookNotFound
import com.readify.readerlibrary.application.service.getbook.BookResponse
import com.readify.readerlibrary.application.service.getbook.ChapterResponse
import com.readify.readerlibrary.application.service.getbook.GetBookRequest
import com.readify.readerlibrary.application.service.getbook.GetBookResponse
import com.readify.readerlibrary.application.service.getbook.GetBookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/v1")
class GetBookController(private val service: GetBookService) {
    @GetMapping("/books/{bookId}")
    fun getBook(@PathVariable bookId: String): ResponseEntity<out Any> =
        service.execute(GetBookRequest(bookId))
            .toHttpResponse()
}

private fun GetBookResponse.toHttpResponse() =
    when (this) {
        BookNotFound -> ResponseEntity.notFound().build()
        is BookResponse -> ResponseEntity.ok(this.toHttpResponse())
    }

private fun BookResponse.toHttpResponse() =
    BookHttpResponse(
        id,
        authorId,
        title,
        cover,
        summary,
        tags,
        HttpMoney(priceAmount, priceCurrency),
        completionPercentage,
        status.toString().toLowerCase(),
        chapters.map { it.toHttpResponse() },
        finishedAt
    )

private fun ChapterResponse.toHttpResponse(): ChapterHttpResponse =
    ChapterHttpResponse(id, title, HttpMoney(priceAmount, priceCurrency), order, excerpt)

data class BookHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("authorId") val authorId: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("cover") val cover: String,
    @JsonProperty("summary") val summary: String,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("price") val price: HttpMoney,
    @JsonProperty("completionPercentage") val completionPercentage: Int,
    @JsonProperty("status") val status: String,
    @JsonProperty("chapters") val chapters: List<ChapterHttpResponse>,
    @JsonProperty("finishedAt") val finishedAt: ZonedDateTime?
)

data class ChapterHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("price") val price: HttpMoney,
    @JsonProperty("order") val order: Int,
    @JsonProperty("excerpt") val excerpt: String?
)