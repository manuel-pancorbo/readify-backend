package com.readify.search.application.service.adduser

import com.readify.search.domain.author.Author
import com.readify.search.domain.author.AuthorId
import com.readify.search.domain.author.AuthorRepository
import com.readify.search.domain.author.FullName
import com.readify.search.domain.author.Username

class AddAuthorService(private val authorRepository: AuthorRepository) {
    fun execute(request: AddAuthorRequest) =
        authorRepository.save(request.toDomain())
            .let { AddAuthorResponse }

}

private fun AddAuthorRequest.toDomain() = Author(AuthorId(authorId), FullName(fullName), Username(username))

data class AddAuthorRequest(val authorId: String, val fullName: String, val username: String)
object AddAuthorResponse