package com.readify.api.readerlibrary.controller.createpayment

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.createpayment.BookNotFoundResponse
import com.readify.readerlibrary.application.service.createpayment.ChapterNotFoundResponse
import com.readify.readerlibrary.application.service.createpayment.CreatePaymentRequest
import com.readify.readerlibrary.application.service.createpayment.CreatePaymentResponse
import com.readify.readerlibrary.application.service.createpayment.CreatePaymentService
import com.readify.readerlibrary.application.service.createpayment.PaymentCreatedResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PostPaymentController(private val createPaymentService: CreatePaymentService) {
    @PostMapping("/readers/{readerId}/payments")
    fun createBook(
        requester: Requester,
        @RequestBody request: PostPaymentHttpRequest,
        @PathVariable readerId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> createPaymentService
                .execute(CreatePaymentRequest(requester.id, request.bookId, request.chapterId))
                .toHttpResponse()
        }
}

private fun CreatePaymentResponse.toHttpResponse() =
    when (this) {
        BookNotFoundResponse, ChapterNotFoundResponse -> ResponseEntity.notFound().build()
        is PaymentCreatedResponse -> ResponseEntity.ok(PostPaymentHttpResponse(id))
    }

data class PostPaymentHttpRequest(
    @JsonProperty("book") val bookId: String,
    @JsonProperty("chapter") val chapterId: String?
)

data class PostPaymentHttpResponse(
    @JsonProperty("id") val id: String
)