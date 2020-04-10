package com.readify.bookpublishing.application.service.common

import com.readify.shared.domain.book.Visibility

enum class BookVisibility { NULL, RESTRICTED, VISIBLE;

    fun toDomain() = when(this) {
        NULL -> Visibility.NULL
        RESTRICTED -> Visibility.RESTRICTED
        VISIBLE -> Visibility.VISIBLE
    }
}