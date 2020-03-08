package com.readify.bookpublishing.application.service.createchapter

data class CreateChapterRequest(val title: String, val content: String, val authorId: String, val bookId: String)