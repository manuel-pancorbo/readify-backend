package com.readify.search.infrastructure.jpa.author

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.readify.IntegrationTest
import com.readify.search.domain.author.Author
import com.readify.search.domain.author.AuthorId
import com.readify.search.domain.author.FullName
import com.readify.search.domain.author.Username
import com.readify.search.infrastructure.domain.author.JpaAuthorRepository
import com.readify.search.infrastructure.jpa.search.SearchJpaAuthorDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class JpaAuthorRepositoryShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: JpaAuthorRepository

    @Autowired
    private lateinit var dataSource: SearchJpaAuthorDataSource

    @BeforeEach
    internal fun setUp() {
        dataSource.deleteAll()
    }

    @Test
    fun `save an user`() {
        val author = Author(AuthorId(authorId), FullName(fullName), Username(username))

        repository.save(author)

        val actual = dataSource.findById(author.id.value)
        assertThat(actual.isPresent).isTrue()
        assertThat(actual.get().id).isEqualTo(author.id.value)
        assertThat(actual.get().fullName).isEqualTo(author.fullName.value)
        assertThat(actual.get().username).isEqualTo(author.username.value)
    }

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val fullName = "Any Full Name"
        private val username = "any-username"
    }
}