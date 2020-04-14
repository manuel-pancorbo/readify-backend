package com.readify.readerlibrary.application.service.common

data class LibraryBookResponse(val type: LibraryBookTypeResponse, val id: String, val chapters: List<String>)
enum class LibraryBookTypeResponse { WHOLE, PARTIAL }