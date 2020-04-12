package com.readify.search.domain.book

data class BookSearchResult(val total: Int, val results: List<Book>) {
    companion object {
        fun empty() = BookSearchResult(0, emptyList())
    }
}