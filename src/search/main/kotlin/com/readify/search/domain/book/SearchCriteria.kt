package com.readify.search.domain.book

data class SearchCriteria(val textFilter: TextFilter?, val tagFilter: TagFilter?, val authorFilter: AuthorFilter?)