package com.readify.api.bookpublishing.controller.updatebook

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
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
    @PatchMapping("/books/{bookId}")
    fun createBook(
        @RequestBody httpRequest: PatchBookHttpRequest,
        @PathVariable bookId: String,
        requester: Requester
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> updateBookService
                .execute(UpdateBookRequest(requester.id, bookId, httpRequest.completionPercentage))
                .toHttpResponse()
        }
}

private fun UpdateBookResponse.toHttpResponse() =
    when (this) {
        BookUpdatedSuccessfully -> ok().build()
        CompletionPercentageOutOfRange -> badRequest().body(
            HttpErrorResponse(
                "bookpublishing.percentage_completion",
                "Completion percentage out of range", "completionPercentage"
            )
        )
        BookNotBelongToAuthorResponse, BookNotFoundResponse -> notFound().build()
    }

data class PatchBookHttpRequest(
    @JsonProperty("completionPercentage") val completionPercentage: Int
)