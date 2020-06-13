package com.readify.api.readerlibrary.controller.getreaderbooks

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.getreaderbooks.BookResponse
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksRequest
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksResponse
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksService
import com.readify.readerlibrary.application.service.getreaderbooks.ReaderBooksResponse
import com.readify.readerlibrary.application.service.getreaderbooks.RequesterAndRequestedReaderAreDifferent
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/v1")
class GetReaderBooksController(private val service: GetReaderBooksService) {
    @GetMapping("/readers/{readerId}/books")
    fun getBook(requester: Requester, @PathVariable readerId: String): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(GetReaderBooksRequest(requester.id, readerId))
                .toHttpResponse()
        }
}

private fun GetReaderBooksResponse.toHttpResponse(): ResponseEntity<Any> =
    when (this) {
        RequesterAndRequestedReaderAreDifferent -> ResponseEntity.notFound().build()
        is ReaderBooksResponse -> ResponseEntity.ok(BooksHttpResponse(books.map { it.toHttpResponse() }))
    }

private fun BookResponse.toHttpResponse() = BookHttpResponse(id, authorId, title, cover, summary, tags,
    HttpMoney(priceAmount, priceCurrency), completionPercentage, status.toString().toLowerCase(), finishedAt)

data class BooksHttpResponse(val books: List<BookHttpResponse>)

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
    @JsonProperty("finishedAt") val finishedAt: ZonedDateTime?
)
