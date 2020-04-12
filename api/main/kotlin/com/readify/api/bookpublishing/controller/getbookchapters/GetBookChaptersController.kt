package com.readify.api.bookpublishing.controller.getbookchapters

import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.getbookchapters.BookChaptersResponse
import com.readify.bookpublishing.application.service.getbookchapters.BookNotFoundResponse
import com.readify.bookpublishing.application.service.getbookchapters.ChapterSummaryResponse
import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersRequest
import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersResponse
import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetBookChaptersController(private val service: GetBookChaptersService) {
    @GetMapping("/authors/{authorId}/books/{bookId}/chapters")
    fun getBookChapters(
        requester: Requester,
        @PathVariable bookId: String,
        @PathVariable authorId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser ->
                if (requester.id != authorId)
                    ResponseEntity.notFound().build()
                else
                    service.execute(GetBookChaptersRequest(requester.id, bookId)).toHttpResponse()
        }
}

private fun GetBookChaptersResponse.toHttpResponse() =
    when (this) {
        BookNotFoundResponse -> ResponseEntity.notFound().build<String>()
        is BookChaptersResponse -> ResponseEntity.ok(HttpBookChaptersResponse(
            chapters.map { it.toHttpResponse() }
        ))
    }

private fun ChapterSummaryResponse.toHttpResponse() =
    HttpChapterSummary(
        id, title, modifiedAt, bookId, authorId, status.toString().toLowerCase(),
        HttpMoney(priceAmount, priceCurrency), order, excerpt
    )
