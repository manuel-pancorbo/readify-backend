package com.readify.userprofile.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.readify.userprofile.domain.user.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class SignUpServiceShould {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val userFactory: UserFactory = mockk()
    private val signUpService = SignUpService(userFactory, userRepository)

    @Test
    fun `throw exception when username is already registered`() {
        val request = SignUpRequest("manuel.pancorbo", "manuel.pancorbo@gmail.com", "dummypass")
        every { userFactory.create(Username("manuel.pancorbo"), Email("manuel.pancorbo@gmail.com")) }
            .throws(UsernameAlreadyRegisteredException("manuel.pancorbo"))

        assertThat { signUpService.execute(request) }
            .isFailure()
            .isInstanceOf(UsernameAlreadyRegisteredException::class)
    }

    @Test
    fun `throw exception when mail is already registered`() {
        val request = SignUpRequest("manu", "manuel.pancorbo@gmail.com", "dummypass")
        every { userFactory.create(Username("manu"), Email("manuel.pancorbo@gmail.com")) }
            .throws(EmailAlreadyRegisteredException("manuel.pancorbo@gmail.com"))

        assertThat { signUpService.execute(request) }
            .isFailure()
            .isInstanceOf(EmailAlreadyRegisteredException::class)
    }

    @Test
    fun `sign up a valid user`() {
        val request = SignUpRequest("manuel.pancorbo", "manuel.pancorbo@gmail.com", "dummypass")
        val user = User(UserId(UUID.randomUUID().toString()),
            Username("manuel.pancorbo"), Email("manuel.pancorbo@gmail.com"))
        every { userFactory.create(Username("manuel.pancorbo"), Email("manuel.pancorbo@gmail.com")) }
            .returns(user)

        val response = signUpService.execute(request)

        assertThat(response.id).isNotNull()
        assertThat(response.username).isEqualTo("manuel.pancorbo")
        assertThat(response.email).isEqualTo("manuel.pancorbo@gmail.com")
    }
}