package com.readify.bookpublishing.application.service.updatebook

data class UpdateBookRequest(val authorId: String, val bookId: String, val completionPercentage: Int)