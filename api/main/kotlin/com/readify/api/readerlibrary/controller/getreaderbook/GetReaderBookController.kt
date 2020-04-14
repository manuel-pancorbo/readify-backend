package com.readify.api.readerlibrary.controller.getreaderbook

import com.readify.api.readerlibrary.controller.getreaderbooks.LibraryBookHttpResponse
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.common.LibraryBookResponse
import com.readify.readerlibrary.application.service.getreaderbook.BookDoesNotBelongToReader
import com.readify.readerlibrary.application.service.getreaderbook.GetReaderBookRequest
import com.readify.readerlibrary.application.service.getreaderbook.GetReaderBookResponse
import com.readify.readerlibrary.application.service.getreaderbook.GetReaderBookService
import com.readify.readerlibrary.application.service.getreaderbook.ReaderBookResponse
import com.readify.readerlibrary.application.service.getreaderbook.RequesterAndRequestedReaderAreDifferent
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetReaderBookController(private val service: GetReaderBookService) {
    @GetMapping("/readers/{readerId}/books/{bookId}")
    fun getBook(
        requester: Requester,
        @PathVariable readerId: String,
        @PathVariable bookId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(GetReaderBookRequest(requester.id, readerId, bookId))
                .toHttpResponse()
        }
}

private fun GetReaderBookResponse.toHttpResponse(): ResponseEntity<Any> =
    when (this) {
        RequesterAndRequestedReaderAreDifferent, BookDoesNotBelongToReader -> ResponseEntity.notFound().build()
        is ReaderBookResponse -> ResponseEntity.ok(book.toHttpResponse())
    }

private fun LibraryBookResponse.toHttpResponse() =
    LibraryBookHttpResponse(
        type.toString().toLowerCase(), id, chapters
    )
