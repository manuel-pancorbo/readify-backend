package com.readify.authentication.infrastructure.jpa.usercredentials

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.readify.api.Application
import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.UserIdentifier
import com.readify.authentication.domain.usercredentials.Username
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(MockKExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaUserCredentialsRepositoryShould {
    @Autowired
    private lateinit var repository: JpaUserCredentialsRepository

    @Autowired
    private lateinit var jpaUserCredentialsDataSource: JpaUserCredentialsDataSource

    @BeforeEach
    internal fun setUp() {
        jpaUserCredentialsDataSource.deleteAll()
    }

    @Test
    fun `save any user credentials`() {
        val userCredentials = anyUserCredentials()

        repository.save(userCredentials)

        val actual = jpaUserCredentialsDataSource.findById("any-user-id")
        assertThat(actual.isPresent)
        assertThat(actual.get().id).isEqualTo(userCredentials.userId.value)
        assertThat(actual.get().username).isEqualTo(userCredentials.username.value)
        assertThat(actual.get().email).isEqualTo(userCredentials.email.value)
        assertThat(actual.get().password).isEqualTo(userCredentials.encodedPassword.value)
    }

    @Test
    fun `find user credentials by username`() {
        val expectedUserCredentials = anyUserCredentials()
        repository.save(expectedUserCredentials)

        val actualUserCredentials = repository.findByUserIdentifier(UserIdentifier("any-username"))

        assertThat(actualUserCredentials).isEqualTo(expectedUserCredentials)
    }

    @Test
    fun `find user credentials by email`() {
        val expectedUserCredentials = anyUserCredentials()
        repository.save(expectedUserCredentials)

        val actualUserCredentials = repository.findByUserIdentifier(UserIdentifier("any-email@gmail.com"))

        assertThat(actualUserCredentials).isEqualTo(expectedUserCredentials)
    }

    @Test
    fun `returns null when user identifier does not match with any saved user credentials`() {
        val actualUserCredentials = repository.findByUserIdentifier(UserIdentifier("any-non-existent-user-identifier"))

        assertThat(actualUserCredentials).isNull()
    }

    private fun anyUserCredentials() =
        UserCredentials(
            UserId("any-user-id"),
            Username("any-username"),
            Email("any-email@gmail.com"),
            EncodedPassword("any-encoded-password")
        )
}