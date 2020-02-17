package com.readify.userprofile.domain.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class UserFactoryShould {

    private val userRepository: UserRepository = mockk()

    private val userFactory = UserFactory(userRepository)

    @Test
    fun `throw exception whenever username already exists`() {
        every { userRepository.findByUsername("manuel.pancorbo") }
            .returns(User(anyId(), Username("manuel.pancorbo"), Email("whatever@gmail.com")))

        assertThat { userFactory.create(Username("manuel.pancorbo"), Email("manuel.pancorbo@gmail.com")) }
            .isFailure()
            .isInstanceOf(UsernameAlreadyRegisteredException::class)
    }

    @Test
    fun `throw exception whenever email already exists`() {
        every { userRepository.findByUsername("manuel.pancorbo") }
            .returns(null)
        every { userRepository.findByEmail("manuel.pancorbo@gmail.com") }
            .returns(User(anyId(), Username("whatever"), Email("manuel.pancorbo@gmail.com")))

        assertThat { userFactory.create(Username("manuel.pancorbo"), Email("manuel.pancorbo@gmail.com")) }
            .isFailure()
            .isInstanceOf(EmailAlreadyRegisteredException::class)
    }

    @Test
    fun `returns a valid user when email and username are uniques`() {
        every { userRepository.findByUsername("manuel.pancorbo") }
            .returns(null)
        every { userRepository.findByEmail("manuel.pancorbo@gmail.com") }
            .returns(null)

        val user = userFactory.create(Username("manuel.pancorbo"), Email("manuel.pancorbo@gmail.com"))

        assertThat(user.email.value).isEqualTo("manuel.pancorbo@gmail.com")
        assertThat(user.username.value).isEqualTo("manuel.pancorbo")
        assertThat(user.id).isNotNull()
    }

    private fun anyId() = UserId(UUID.randomUUID().toString())
}