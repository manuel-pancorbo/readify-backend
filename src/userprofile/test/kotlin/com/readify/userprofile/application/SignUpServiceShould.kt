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

    private val userRepository: UserRepository = mockk()
    private val signUpService = SignUpService(userRepository)

    @Test
    fun `throw exception when username is already registered`() {
        val request = SignUpRequest("manuel.pancorbo", "manuel.pancorbo@gmail.com", "dummypass")
        every { userRepository.findByUsername("manuel.pancorbo") }
            .returns(buildUser("manuel.pancorbo", "othermail@gmail.com"))

        assertThat { signUpService.execute(request) }
            .isFailure()
            .isInstanceOf(UsernameAlreadyRegisteredException::class)
    }

    @Test
    fun `throw exception when mail is already registered`() {
        val request = SignUpRequest("manu", "manuel.pancorbo@gmail.com", "dummypass")
        every { userRepository.findByUsername("manu") } returns null
        every { userRepository.findByEmail("manuel.pancorbo@gmail.com") }
            .returns(buildUser("manu", "manuel.pancorbo@gmail.com"))

        assertThat { signUpService.execute(request) }
            .isFailure()
            .isInstanceOf(EmailAlreadyRegisteredException::class)
    }

    @Test
    fun `register a valid user`() {
        val request = SignUpRequest("manuel.pancorbo", "manuel.pancorbo@gmail.com", "dummypass")
        every { userRepository.findByUsername("manuel.pancorbo") } returns null
        every { userRepository.findByEmail("manuel.pancorbo@gmail.com") } returns null
        every { userRepository.save(any()) }
            .returns(buildUser("manuel.pancorbo", "manuel.pancorbo@gmail.com"))

        val response = signUpService.execute(request)

        assertThat(response.id).isNotNull()
        assertThat(response.username).isEqualTo("manuel.pancorbo")
        assertThat(response.email).isEqualTo("manuel.pancorbo@gmail.com")
    }


    private fun buildUser(username: String, email: String, password: String = "dummy-password") =
        User(
            id = UserId(UUID.randomUUID().toString()),
            username = Username(username),
            email = Email(email),
            password = Password(password)
        )
}