package com.readify.bookpublishing.application.service.updatechapter

data class UpdateBookChapterRequest(val authorId: String, val bookId: String, val chapterId: String, val status: String)