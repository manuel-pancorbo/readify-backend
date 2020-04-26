package com.readify.api.bookpublishing.controller.getauthorbook

import com.readify.api.bookpublishing.controller.common.HttpBookResponse
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.getbook.AuthorBookFoundResponse
import com.readify.bookpublishing.application.service.getbook.AuthorBookNotFoundResponse
import com.readify.bookpublishing.application.service.getbook.GetAuthorBookRequest
import com.readify.bookpublishing.application.service.getbook.GetAuthorBookResponse
import com.readify.bookpublishing.application.service.getbook.GetAuthorBookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetAuthorBookController(private val service: GetAuthorBookService) {
    @GetMapping("/authors/{authorId}/books/{bookId}")
    fun getBook(
        requester: Requester,
        @PathVariable bookId: String,
        @PathVariable authorId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser ->
                if (requester.id != authorId)
                    ResponseEntity.notFound().build()
                else
                    service.execute(GetAuthorBookRequest(requester.id, bookId)).toHttpResponse()
        }
}

private fun GetAuthorBookResponse.toHttpResponse() =
    when (this) {
        AuthorBookNotFoundResponse -> ResponseEntity.notFound().build<String>()
        is AuthorBookFoundResponse -> ResponseEntity.ok(
            HttpBookResponse(
                bookId, authorId, title, summary, cover, tags, HttpMoney(priceAmount, priceCurrency),
                status.toString().toLowerCase(), visibility.toString().toLowerCase(), completionPercentage, finishedAt
            )
        )
    }
