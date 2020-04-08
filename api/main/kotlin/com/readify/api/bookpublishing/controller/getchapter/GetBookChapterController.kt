package com.readify.api.bookpublishing.controller.getchapter

import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.bookpublishing.application.service.getchapterservice.ChapterNotFoundResponse
import com.readify.bookpublishing.application.service.getchapterservice.ChapterResponse
import com.readify.bookpublishing.application.service.getchapterservice.GetChapterRequest
import com.readify.bookpublishing.application.service.getchapterservice.GetChapterResponse
import com.readify.bookpublishing.application.service.getchapterservice.GetChapterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetBookChapterController(private val getChapterService: GetChapterService) {
    @GetMapping("/books/{bookId}/chapters/{chapterId}")
    fun createBook(
        requester: Requester,
        @PathVariable bookId: String,
        @PathVariable chapterId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> getChapterService.execute(GetChapterRequest(requester.id, bookId, chapterId))
                .toHttpResponse()
        }
}

private fun GetChapterResponse.toHttpResponse() =
    when (this) {
        ChapterNotFoundResponse -> ResponseEntity.notFound().build()
        is ChapterResponse -> ResponseEntity.ok(
            GetChapterHttpResponse(
                id, title, content, modifiedAt, bookId, authorId,
                status.toString().toLowerCase(), HttpMoney(priceAmount, priceCurrency), order, excerpt
            )
        )
    }
