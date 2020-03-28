package com.readify.api.bookpublishing.controller.createbook

import com.readify.api.bookpublishing.controller.common.HttpBookResponse
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.createbook.CreateBookRequest
import com.readify.bookpublishing.application.service.createbook.BookCreatedSuccessfullyResponse
import com.readify.bookpublishing.application.service.createbook.InvalidCurrencyResponse
import com.readify.bookpublishing.application.service.createbook.CreateBookResponse
import com.readify.bookpublishing.application.service.createbook.CreateBookService
import com.readify.shared.infrastructure.controller.error.HttpErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PostBookController(private val createBookService: CreateBookService) {
    @PostMapping("/books")
    fun createBook(
        @RequestBody httpBookRequest: HttpBookRequest,
        requester: Requester
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> {
                createBookService
                    .execute(httpBookRequest.toCreateBookRequest(requester))
                    .toHttpResponse()
            }
        }
}

private fun HttpBookRequest.toCreateBookRequest(requester: LoggedUser) =
    CreateBookRequest(requester.id, title, summary, cover, tags, price.amount, price.currency)

private fun CreateBookResponse.toHttpResponse() =
    when(this) {
        InvalidCurrencyResponse -> ResponseEntity.badRequest()
            .body(HttpErrorResponse("bookpublishing.currency_not_supported", "Currency not supported", "price"))
        is BookCreatedSuccessfullyResponse -> ResponseEntity.ok(
            HttpBookResponse(
                bookId, authorId, title, summary,
                cover, tags, HttpMoney(priceAmount, priceCurrency), status.toString().toLowerCase(),
                visibility.toString().toLowerCase(), completionPercentage, finishedAt
            )
        )
    }
