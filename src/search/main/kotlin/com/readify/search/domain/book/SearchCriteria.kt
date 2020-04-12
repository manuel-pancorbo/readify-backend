package com.readify.search.domain.book

data class SearchCriteria(
    val textFilter: TextFilter? = null,
    val tagFilter: TagFilter? = null,
    val authorFilter: AuthorFilter? = null
)