package com.readify.bookpublishing.domain.book

import java.util.UUID

data class AuthorId(val value: String)
data class Title(val value: String)
data class Summary(val value: String)
data class Cover(val value: String)
data class Tags(val value: List<String>)
data class CompletionPercentage(val value: Int) {
    init {
        if (value !in 0..100) throw CompletionPercentageOutOfRangeException
    }

    fun isFinished() = value == 100

    companion object {
        fun empty() = CompletionPercentage(0)
        fun finished() = CompletionPercentage(100)
    }
}
data class BookId(val value: String) {
    init {
        UUID.fromString(value)
    }
}