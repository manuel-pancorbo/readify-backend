package com.readify.readerlibrary.application.service.getbooksbyids

import com.readify.readerlibrary.application.service.common.BookResponse

class GetBooksByIdsService {
    fun execute(request: GetBooksByIdsRequest): GetBooksByIdsResponse {
        TODO("Not yet implemented")
    }

}

data class GetBooksByIdsRequest(val ids: List<String>)
data class GetBooksByIdsResponse(val books: List<BookResponse>)