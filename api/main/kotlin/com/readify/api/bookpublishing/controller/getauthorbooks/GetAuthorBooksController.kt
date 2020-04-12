package com.readify.api.bookpublishing.controller.getauthorbooks

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpBookResponse
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.common.BookResponse
import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksRequest
import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksResponse
import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetAuthorBooksController(private val service: GetAuthorBooksService) {
    @GetMapping("/authors/{authorId}/books")
    fun getBook(requester: Requester, @PathVariable authorId: String): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser ->
                if (requester.id != authorId)
                    ResponseEntity.notFound().build()
                else
                    service.execute(GetAuthorBooksRequest(requester.id)).toHttpResponse()
        }
}

private fun GetAuthorBooksResponse.toHttpResponse() =
    ResponseEntity.ok(HttpAuthorBooksResponse(books.map { it.toHttpResponse() }))

private fun BookResponse.toHttpResponse() =
    HttpBookResponse(
        bookId, authorId, title, summary, cover, tags, HttpMoney(priceAmount, priceCurrency),
        status.toString().toLowerCase(), visibility.toString().toLowerCase(), completionPercentage, finishedAt
    )

data class HttpAuthorBooksResponse(@JsonProperty("books") val books: List<HttpBookResponse>)