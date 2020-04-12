package com.readify.search.domain.book

import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime

data class Book(
    val bookId: BookId,
    val authorId: AuthorId,
    val title: Title,
    val cover: Cover,
    val summary: Summary,
    val tags: Tags,
    val price: Money,
    val completionPercentage: CompletionPercentage,
    val status: BookStatus,
    val finishedAt: ZonedDateTime?
)

enum class BookStatus { IN_PROGRESS, FINISHED }
data class BookId(val value: String)
data class AuthorId(val value: String)
data class Title(val value: String)
data class Cover(val value: String)
data class Summary(val value: String)
data class Tags(val value: List<String>)
data class CompletionPercentage(val value: Int)