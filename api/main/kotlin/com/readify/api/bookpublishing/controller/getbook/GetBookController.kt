package com.readify.api.bookpublishing.controller.getbook

import com.readify.api.bookpublishing.controller.common.HttpBookResponse
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.getbook.BookFoundResponse
import com.readify.bookpublishing.application.service.getbook.BookNotFoundResponse
import com.readify.bookpublishing.application.service.getbook.GetBookRequest
import com.readify.bookpublishing.application.service.getbook.GetBookResponse
import com.readify.bookpublishing.application.service.getbook.GetBookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetBookController(private val service: GetBookService) {
    @GetMapping("/books/{bookId}")
    fun getBook(requester: Requester, @PathVariable bookId: String): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(GetBookRequest(requester.id, bookId)).toHttpResponse()
        }
}

private fun GetBookResponse.toHttpResponse() =
    when (this) {
        BookNotFoundResponse -> ResponseEntity.notFound().build<String>()
        is BookFoundResponse -> ResponseEntity.ok(
            HttpBookResponse(
                bookId, authorId, title, summary, cover, tags, HttpMoney(priceAmount, priceCurrency),
                status.toString().toLowerCase(), visibility.toString().toLowerCase(), completionPercentage, finishedAt
            )
        )
    }
