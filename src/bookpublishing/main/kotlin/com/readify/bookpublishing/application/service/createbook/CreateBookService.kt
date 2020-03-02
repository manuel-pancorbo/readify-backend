package com.readify.bookpublishing.application.service.createbook

class CreateBookService {
    fun execute(request: CreateBookRequest): CreateBookResponse = CreateBookResponse("", "", "", "", emptyList())
}

data class CreateBookRequest(
    val authorId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>
)

data class CreateBookResponse(
    val id: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>
)