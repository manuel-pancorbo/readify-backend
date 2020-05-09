package com.readify.api.readerlibrary.controller.getreaderbookchapter

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.getreaderbookchapter.BookChapterDoesNotBelongToReader
import com.readify.readerlibrary.application.service.getreaderbookchapter.FullChapterResponse
import com.readify.readerlibrary.application.service.getreaderbookchapter.GetReaderBookChapterRequest
import com.readify.readerlibrary.application.service.getreaderbookchapter.GetReaderBookChapterResponse
import com.readify.readerlibrary.application.service.getreaderbookchapter.GetReaderBookChapterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/v1")
class GetReaderBookChapterController(private val service: GetReaderBookChapterService) {
    @GetMapping("/readers/{readerId}/books/{bookId}/chapters/{chapterId}")
    fun getBook(
        requester: Requester,
        @PathVariable readerId: String,
        @PathVariable bookId: String,
        @PathVariable chapterId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(GetReaderBookChapterRequest(readerId, bookId, chapterId))
                .toHttpResponse()
        }
}

private fun GetReaderBookChapterResponse.toHttpResponse() =
    when (this) {
        BookChapterDoesNotBelongToReader -> ResponseEntity.notFound().build<String>()
        is FullChapterResponse -> ResponseEntity.ok(FullChapterHttpResponse(id, title, content,
            HttpMoney(priceAmount, priceCurrency), authorId, bookId, modifiedAt, order, excerpt, publishedAt))
    }

data class FullChapterHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("price") val price: HttpMoney,
    @JsonProperty("author") val author: String,
    @JsonProperty("book") val book: String,
    @JsonProperty("modifiedAt") val modifiedAt: ZonedDateTime,
    @JsonProperty("order") val order: Int,
    @JsonProperty("excerpt") val excerpt: String?,
    @JsonProperty("publishedAt") val publishedAt: ZonedDateTime
)
