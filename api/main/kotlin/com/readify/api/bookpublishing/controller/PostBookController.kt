package com.readify.api.bookpublishing.controller

import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.createbook.CreateBookRequest
import com.readify.bookpublishing.application.service.createbook.CreateBookResponse
import com.readify.bookpublishing.application.service.createbook.CreateBookService
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
    fun generateToken(
        @RequestBody httpBookRequest: HttpBookRequest,
        requester: Requester
    ): ResponseEntity<HttpBookResponse> =
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
    CreateBookRequest(requester.id, this.title, this.summary, this.cover, this.tags)

private fun CreateBookResponse.toHttpResponse() =
    ResponseEntity.ok(HttpBookResponse(this.id, this.title, this.summary, this.cover, this.tags))
