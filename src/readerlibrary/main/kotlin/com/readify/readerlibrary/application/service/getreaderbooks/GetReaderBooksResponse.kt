package com.readify.readerlibrary.application.service.getreaderbooks

import java.time.ZonedDateTime

sealed class GetReaderBooksResponse
object RequesterAndRequestedReaderAreDifferent : GetReaderBooksResponse()
data class ReaderBooksResponse(val books: List<BookResponse>) : GetReaderBooksResponse()

data class BookResponse(
    val id: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String,
    val completionPercentage: Int,
    val status: BookStatusResponse,
    val finishedAt: ZonedDateTime?
)

enum class BookStatusResponse { IN_PROGRESS, FINISHED }
