package com.readify.bookpublishing.application.service.createchapter

import java.time.ZonedDateTime

class CreateChapterService {
    fun execute(request: CreateChapterRequest): CreateChapterResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class CreateChapterRequest(val title: String, val content: String, val authorId: String, val bookId: String)

sealed class CreateChapterResponse
object BookNotFoundResponse : CreateChapterResponse()
object BookNotBelongToAuthorResponse : CreateChapterResponse()
data class ChapterCreatedResponse(
    val id: String,
    val title: String,
    val content: String,
    val modifiedAt: ZonedDateTime,
    val authorId: String,
    val bookId: String
): CreateChapterResponse()