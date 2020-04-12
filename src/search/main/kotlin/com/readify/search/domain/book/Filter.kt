package com.readify.search.domain.book

sealed class Filter
data class TextFilter(val value: String): Filter()
data class TagFilter(val value: String): Filter()
data class AuthorFilter(val value: String): Filter()