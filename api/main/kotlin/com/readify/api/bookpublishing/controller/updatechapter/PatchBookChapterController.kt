package com.readify.api.bookpublishing.controller.updatechapter

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.BookChapterHttpResponse
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.updatechapter.BookChapterNotFoundResponse
import com.readify.bookpublishing.application.service.updatechapter.BookChapterUpdatedResponse
import com.readify.bookpublishing.application.service.updatechapter.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.updatechapter.InvalidChapterStatusResponse
import com.readify.bookpublishing.application.service.updatechapter.UpdateBookChapterRequest
import com.readify.bookpublishing.application.service.updatechapter.UpdateBookChapterResponse
import com.readify.bookpublishing.application.service.updatechapter.UpdateBookChapterService
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
class PatchBookChapterController(private val service: UpdateBookChapterService) {
    @PatchMapping("/books/{bookId}/chapters/{chapterId}")
    fun updateChapter(
        @RequestBody httpRequest: PatchBookChapterHttpRequest,
        @PathVariable bookId: String,
        @PathVariable chapterId: String,
        requester: Requester
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service
                .execute(httpRequest.toServiceRequest(requester, bookId, chapterId))
                .toHttpResponse()
        }
}

private fun UpdateBookChapterResponse.toHttpResponse(): ResponseEntity<out Any> =
    when (this) {
        BookChapterNotFoundResponse, BookNotBelongToAuthorResponse -> notFound().build()
        InvalidChapterStatusResponse -> badRequest()
            .body(HttpErrorResponse("bookpublishing.chapter.invalid-status", "Invalid chapter status", "status"))
        is BookChapterUpdatedResponse -> ok(
            BookChapterHttpResponse(
                id, title, content, modifiedAt, bookId, authorId,
                status.toString().toLowerCase(), HttpMoney(priceAmount, priceCurrency)
            )
        )
    }

private fun PatchBookChapterHttpRequest.toServiceRequest(requester: LoggedUser, bookId: String, chapterId: String) =
    UpdateBookChapterRequest(requester.id, bookId, chapterId, status)

data class PatchBookChapterHttpRequest(
    @JsonProperty("status") val status: String
)
