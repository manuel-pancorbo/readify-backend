package com.readify.api.readerlibrary.controller.common

data class LibraryBookHttpResponse(val type: String, val id: String, val chapters: List<String>)