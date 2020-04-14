package com.readify.readerlibrary.application.service.getreaderbooks

import com.readify.readerlibrary.application.service.common.LibraryBookResponse

sealed class GetReaderBooksResponse
object RequesterAndRequestedReaderAreDifferent : GetReaderBooksResponse()
data class ReaderBooksResponse(val books: List<LibraryBookResponse>) : GetReaderBooksResponse()
