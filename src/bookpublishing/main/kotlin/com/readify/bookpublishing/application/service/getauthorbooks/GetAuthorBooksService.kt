package com.readify.bookpublishing.application.service.getauthorbooks

import com.readify.bookpublishing.application.service.common.BookResponse

class GetAuthorBooksService {
    fun execute(request: GetAuthorBooksRequest): GetAuthorBooksResponse {
        TODO("Not yet implemented")
    }

}

data class GetAuthorBooksRequest(val authorId: String)
data class GetAuthorBooksResponse(val books: List<BookResponse>)