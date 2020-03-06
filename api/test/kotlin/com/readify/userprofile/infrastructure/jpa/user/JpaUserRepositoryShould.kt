package com.readify.userprofile.infrastructure.jpa.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.readify.api.Application
import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.usercredentials.PlainPassword
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@ExtendWith(MockKExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaUserRepositoryShould {
    @Autowired
    private lateinit var repository: JpaUserRepository

    @Autowired
    private lateinit var dataSource: JpaUserDataSource

    @BeforeEach
    internal fun setUp() {
        dataSource.deleteAll()
    }

    @Test
    fun `save an user`() {
        val user = anyUser()

        repository.save(user)

        val actual = dataSource.findById(user.id.value)
        assertThat(actual.isPresent).isTrue()
        assertThat(actual.get().id).isEqualTo(user.id.value)
        assertThat(actual.get().username).isEqualTo(user.username.value)
        assertThat(actual.get().email).isEqualTo(user.email.value)
    }

    @Test
    fun `find an user by username`() {
        val user = anyUser()
        repository.save(user)

        val actual = repository.findByUsername("any-username")

        assertThat(actual).isNotNull()
        assertThat(actual?.id).isEqualTo(user.id)
        assertThat(actual?.username).isEqualTo(user.username)
        assertThat(actual?.email).isEqualTo(user.email)
    }

    @Test
    fun `return null when username does not belong to any user`() {
        val actual = repository.findByUsername("any-non-existent-username")

        assertThat(actual).isNull()
    }

    @Test
    fun `find an user by email`() {
        val user = anyUser()
        repository.save(user)

        val actual = repository.findByEmail("any-email@gmail.com")

        assertThat(actual).isNotNull()
        assertThat(actual?.id).isEqualTo(user.id)
        assertThat(actual?.username).isEqualTo(user.username)
        assertThat(actual?.email).isEqualTo(user.email)
    }

    @Test
    fun `return null when email does not belong to any user`() {
        val actual = repository.findByEmail("any-non-existent-email@gmail.com")

        assertThat(actual).isNull()
    }

    private fun anyUser() =
        User(
            UserId(UUID.randomUUID().toString()),
            Username("any-username"),
            Email("any-email@gmail.com"),
            PlainPassword(("any-password"))
        )
}