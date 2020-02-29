package com.readify.authentication.application.service.createaccesstoken

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenRequest
import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenService
import com.readify.authentication.domain.accesstoken.AccessTokenGenerator
import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.InvalidUserCredentialsException
import com.readify.authentication.domain.usercredentials.PasswordEncoderService
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.UserIdentifier
import com.readify.authentication.domain.usercredentials.Username
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class CreateAccessTokenServiceShould {

    private val userCredentialsRepository: UserCredentialsRepository = mockk()
    private val passwordEncoderService: PasswordEncoderService = mockk(relaxed = true)
    private val accessTokenGenerator: AccessTokenGenerator = mockk()
    private val createAccessTokenService =
        CreateAccessTokenService(
            userCredentialsRepository,
            passwordEncoderService,
            accessTokenGenerator
        )

    @Test
    fun `throw exception when credentials provided are not valid`() {
        every { userCredentialsRepository.findByUserIdentifierAndPassword(UserIdentifier("manu"), any()) } returns null

        assertThat { createAccessTokenService.execute(
            CreateAccessTokenRequest(
                "manu",
                "password"
            )
        ) }
            .isFailure()
            .isInstanceOf(InvalidUserCredentialsException::class)
    }

    @Test
    fun `return a valid access token when credentials are valid`() {
        every { userCredentialsRepository.findByUserIdentifierAndPassword(UserIdentifier("manu"), any()) }
            .returns(anyUserCredentials())
        every { accessTokenGenerator.generate(anyUserCredentials()) } returns "anytoken"

        val response = createAccessTokenService.execute(
            CreateAccessTokenRequest(
                "manu",
                "any plain password"
            )
        )

        assertThat(response.token).isEqualTo("anytoken")
    }

    private fun anyUserCredentials() =
        UserCredentials(
            UserId("747ffcd7-67be-4640-84e9-1f80aba1d102"),
            Username("manu"),
            Email("manuelpancorbo@gmail.com"),
            EncodedPassword("any encoded password")
        )
}