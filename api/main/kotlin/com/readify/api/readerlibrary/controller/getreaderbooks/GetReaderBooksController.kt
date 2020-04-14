package com.readify.api.readerlibrary.controller.getreaderbooks

import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.common.LibraryBookResponse
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksRequest
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksResponse
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksService
import com.readify.readerlibrary.application.service.getreaderbooks.ReaderBooksResponse
import com.readify.readerlibrary.application.service.getreaderbooks.RequesterAndRequestedReaderAreDifferent
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetReaderBooksController(private val service: GetReaderBooksService) {
    @GetMapping("/readers/{readerId}/books")
    fun getBook(requester: Requester, @PathVariable readerId: String): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(GetReaderBooksRequest(requester.id, readerId))
                .toHttpResponse()
        }
}

private fun GetReaderBooksResponse.toHttpResponse(): ResponseEntity<Any> =
    when (this) {
        RequesterAndRequestedReaderAreDifferent -> ResponseEntity.notFound().build()
        is ReaderBooksResponse -> ResponseEntity.ok(LibraryBooksHttpResponse(books.map { it.toHttpResponse() }))
    }

private fun LibraryBookResponse.toHttpResponse() = LibraryBookHttpResponse(type.toString().toLowerCase(), id, chapters)

data class LibraryBooksHttpResponse(val books: List<LibraryBookHttpResponse>)
data class LibraryBookHttpResponse(val type: String, val id: String, val chapters: List<String>)