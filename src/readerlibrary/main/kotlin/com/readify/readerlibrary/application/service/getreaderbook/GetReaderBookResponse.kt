package com.readify.readerlibrary.application.service.getreaderbook

import com.readify.readerlibrary.application.service.common.LibraryBookResponse

sealed class GetReaderBookResponse
object RequesterAndRequestedReaderAreDifferent : GetReaderBookResponse()
object BookDoesNotBelongToReader : GetReaderBookResponse()
data class ReaderBookResponse(val book: LibraryBookResponse) : GetReaderBookResponse()