package com.readify.userprofile.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import com.readify.userprofile.domain.user.FullName
import com.readify.userprofile.domain.user.Image
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserFactory
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.user.UsernameAlreadyRegisteredException
import com.readify.userprofile.domain.user.Website
import com.readify.userprofile.domain.usercredentials.PlainPassword
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
        val request = SignUpRequest("manuel.pancorbo", "manuel.pancorbo@gmail.com", "dummypass", "Manuel Pancorbo",
            "any image", "https://manuel.pancorbo")
        every {
            userFactory.create(
                Username("manuel.pancorbo"),
                Email("manuel.pancorbo@gmail.com"),
                FullName("Manuel Pancorbo"),
                Image("any image"),
                Website("https://manuel.pancorbo"),
                PlainPassword("dummypass")
            )
        }
            .throws(UsernameAlreadyRegisteredException("manuel.pancorbo"))

        assertThat { signUpService.execute(request) }
            .isFailure()
            .isInstanceOf(UsernameAlreadyRegisteredException::class)
    }

    @Test
    fun `throw exception when mail is already registered`() {
        val request = SignUpRequest("manu", "manuel.pancorbo@gmail.com", "dummypass", "Manuel Pancorbo",
            "any image", "https://manuel.pancorbo")
        every {
            userFactory.create(
                Username("manu"),
                Email("manuel.pancorbo@gmail.com"),
                FullName("Manuel Pancorbo"),
                Image("any image"),
                Website("https://manuel.pancorbo"),
                PlainPassword("dummypass")
            )
        }
            .throws(EmailAlreadyRegisteredException("manuel.pancorbo@gmail.com"))

        assertThat { signUpService.execute(request) }
            .isFailure()
            .isInstanceOf(EmailAlreadyRegisteredException::class)
    }

    @Test
    fun `sign up a valid user`() {
        val request = SignUpRequest("manuel.pancorbo", "manuel.pancorbo@gmail.com", "dummypass", "Manuel Pancorbo",
            "any image", "https://manuel.pancorbo")
        val user = User(
            UserId(UUID.randomUUID().toString()),
            Username("manuel.pancorbo"),
            Email("manuel.pancorbo@gmail.com"),
            FullName("Manuel Pancorbo"),
            Image("any image"),
            Website("https://manuel.pancorbo"),
            null
        )
        every {
            userFactory.create(
                Username("manuel.pancorbo"),
                Email("manuel.pancorbo@gmail.com"),
                FullName("Manuel Pancorbo"),
                Image("any image"),
                Website("https://manuel.pancorbo"),
                PlainPassword("dummypass")
            )
        }
            .returns(user)

        val response = signUpService.execute(request)

        assertThat(response).isEqualTo(SignUpResponse)
    }
}