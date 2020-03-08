package com.readify.bookpublishing.application.service.createchapter

import java.time.ZonedDateTime

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
) : CreateChapterResponse()