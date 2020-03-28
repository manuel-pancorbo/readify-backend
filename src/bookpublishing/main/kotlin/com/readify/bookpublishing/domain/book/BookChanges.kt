package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.money.Money

data class BookChanges(
    val title: Title? = null,
    val summary: Summary? = null,
    val cover: Cover? = null,
    val tags: Tags? = null,
    val price: Money? = null,
    val visibility: Visibility? = null,
    val completionPercentage: CompletionPercentage? = null
)
