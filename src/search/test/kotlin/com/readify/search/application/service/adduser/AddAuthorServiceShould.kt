package com.readify.search.application.service.adduser

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.search.domain.author.Author
import com.readify.search.domain.author.AuthorId
import com.readify.search.domain.author.AuthorRepository
import com.readify.search.domain.author.FullName
import com.readify.search.domain.author.Username
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID

class AddAuthorServiceShould {
    private val authorRepository: AuthorRepository = mockk(relaxed = true)
    private val service = AddAuthorService(authorRepository)

    @Test
    internal fun `return ok response after replicate author successfully`() {
        val author = Author(AuthorId(authorId), FullName(fullName), Username(username))

        val response = service.execute(AddAuthorRequest(authorId, fullName, username))

        assertThat(response).isEqualTo(AddAuthorResponse)
        verify { authorRepository.save(author) }
    }

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val fullName = "Any Full Name"
        private val username = "any-username"
    }
}