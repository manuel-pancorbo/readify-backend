package com.readify.api.bookpublishing.controller

import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.createbook.PublishBookRequest
import com.readify.bookpublishing.application.service.createbook.PublishBookResponse
import com.readify.bookpublishing.application.service.createbook.PublishBookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PostBookController(private val publishBookService: PublishBookService) {
    @PostMapping("/books")
    fun generateToken(
        @RequestBody httpBookRequest: HttpBookRequest,
        requester: Requester
    ): ResponseEntity<HttpBookResponse> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> {
                publishBookService
                    .execute(httpBookRequest.toCreateBookRequest(requester))
                    .toHttpResponse()
            }
        }
}

private fun HttpBookRequest.toCreateBookRequest(requester: LoggedUser) =
    PublishBookRequest(requester.id, title, summary, cover, tags)

private fun PublishBookResponse.toHttpResponse() =
    ResponseEntity.ok(HttpBookResponse(bookId, authorId, title, summary, cover, tags))
