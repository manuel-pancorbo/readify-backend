package com.readify.application.service.searchbooks

import com.readify.search.application.service.common.ApplicationBook

class SearchBooksService {
    fun execute(request: SearchBooksRequest): SearchBooksResponse {
        TODO("Not yet implemented")
    }

}

data class SearchBooksRequest(val text: String? = null, val tag: String? = null, val authorId: String? = null)
data class SearchBooksResponse(val total: Int, val results: List<ApplicationBook>)