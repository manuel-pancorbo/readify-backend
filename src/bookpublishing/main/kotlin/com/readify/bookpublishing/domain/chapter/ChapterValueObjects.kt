package com.readify.bookpublishing.domain.chapter

import java.util.StringTokenizer
import java.util.UUID

data class Title(val value: String)
data class Content(val value: String) {
    val wordCount: Int = StringTokenizer(value).countTokens()

    init {
        if (wordCount > 10000) throw IllegalArgumentException()
    }
}

data class ChapterId(val value: String) {
    init {
        UUID.fromString(value)
    }
}
data class Order(val value: Int) {
    init {
        if (value < 1) throw IllegalArgumentException()
    }
}
data class Excerpt(val value: String) {
    private val wordCount: Int = StringTokenizer(value).countTokens()

    init {
        if (wordCount > 200) throw IllegalArgumentException()
    }
}