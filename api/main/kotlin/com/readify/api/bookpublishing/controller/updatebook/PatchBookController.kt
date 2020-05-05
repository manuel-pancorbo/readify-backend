package com.readify.api.bookpublishing.controller.updatebook

import com.readify.api.bookpublishing.controller.common.HttpBookResponse
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.application.service.updatebook.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.updatebook.BookNotFoundResponse
import com.readify.bookpublishing.application.service.updatebook.BookUpdatedSuccessfully
import com.readify.bookpublishing.application.service.updatebook.CompletionPercentageOutOfRange
import com.readify.bookpublishing.application.service.updatebook.UpdateBookRequest
import com.readify.bookpublishing.application.service.updatebook.UpdateBookResponse
import com.readify.bookpublishing.application.service.updatebook.UpdateBookService
import com.readify.shared.infrastructure.controller.error.HttpErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PatchBookController(private val updateBookService: UpdateBookService) {
    @PatchMapping("/authors/{authorId}/books/{bookId}")
    fun createBook(
        requester: Requester,
        @RequestBody httpRequest: PatchBookHttpRequest,
        @PathVariable bookId: String,
        @PathVariable authorId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser ->
                if (requester.id != authorId)
                    notFound().build()
                else
                    updateBookService
                        .execute(httpRequest.toServiceRequest(requester, bookId))
                        .toHttpResponse()
        }
}

private fun PatchBookHttpRequest.toServiceRequest(requester: LoggedUser, bookId: String) =
    UpdateBookRequest(
        requester.id, bookId, title, summary, cover, tags ?: emptyList(), price?.amount, price?.currency,
        visibility.toServiceVisibility(), completionPercentage
    )

private fun String?.toServiceVisibility() = when (this) {
    "null" -> BookVisibility.NULL
    "restricted" -> BookVisibility.RESTRICTED
    "visible" -> BookVisibility.VISIBLE
    else -> null
}

private fun UpdateBookResponse.toHttpResponse() =
    when (this) {
        is BookUpdatedSuccessfully -> ok(
            HttpBookResponse(
                bookId, authorId, title, summary,
                cover, tags, HttpMoney(priceAmount, priceCurrency), status.toString().toLowerCase(),
                visibility.toString().toLowerCase(), completionPercentage, finishedAt
            )
        )
        CompletionPercentageOutOfRange -> badRequest().body(
            HttpErrorResponse(
                "bookpublishing.percentage_completion",
                "Completion percentage out of range",
                "completionPercentage"
            )
        )
        BookNotBelongToAuthorResponse, BookNotFoundResponse -> notFound().build()
    }
