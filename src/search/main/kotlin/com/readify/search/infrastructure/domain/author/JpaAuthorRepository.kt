package com.readify.search.infrastructure.domain.author

import com.readify.search.domain.author.Author
import com.readify.search.domain.author.AuthorRepository
import com.readify.search.infrastructure.jpa.search.JpaAuthor
import com.readify.search.infrastructure.jpa.search.SearchJpaAuthorDataSource

class JpaAuthorRepository(private val dataSource: SearchJpaAuthorDataSource) : AuthorRepository {
    override fun save(author: Author) {
        dataSource.save(author.toJpa())
    }
}

private fun Author.toJpa() = JpaAuthor(id.value, fullName.value, username.value)
