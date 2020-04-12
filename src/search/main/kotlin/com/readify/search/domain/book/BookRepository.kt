package com.readify.search.domain.book

interface BookRepository {
    fun save(book: Book)
    fun search(searchCriteria: SearchCriteria): BookSearchResult
}
