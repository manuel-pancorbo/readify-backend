package com.readify.bookpublishing.application.service.getchapterservice

import com.readify.bookpublishing.application.service.common.ChapterStatus
import java.time.ZonedDateTime

class GetChapterService {
    fun execute(request: GetChapterRequest): GetChapterResponse {
        TODO("Not yet implemented")
    }

}

sealed class GetChapterResponse
object BookNotFoundResponse : GetChapterResponse()
object ChapterNotFoundResponse : GetChapterResponse()
data class ChapterResponse(
    val id: String,
    val title: String,
    val content: String,
    val modifiedAt: ZonedDateTime,
    val authorId: String,
    val bookId: String,
    val status: ChapterStatus,
    val priceAmount: Float,
    val priceCurrency: String,
    val order: Int,
    val excerpt: String?
) : GetChapterResponse()
