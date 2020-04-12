package com.readify.search.domain.author

data class Author(val id: AuthorId, val fullName: FullName, val username: Username)

data class AuthorId(val value: String)
data class FullName(val value: String)
data class Username(val value: String)


