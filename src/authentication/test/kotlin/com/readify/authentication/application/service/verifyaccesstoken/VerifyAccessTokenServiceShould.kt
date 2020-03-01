package com.readify.authentication.application.service.verifyaccesstoken

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.readify.authentication.domain.accesstoken.AccessTokenVerifier
import com.readify.authentication.domain.accesstoken.ExpiredAccessTokenException
import com.readify.authentication.domain.accesstoken.InvalidAccessTokenException
import com.readify.authentication.domain.accesstoken.UserInformation
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class VerifyAccessTokenServiceShould {
    private val tokenVerifier : AccessTokenVerifier = mockk()
    private val service = VerifyAccessTokenService(tokenVerifier)

    @Test
    fun `throw exception when token is invalid`() {
        every { tokenVerifier.verify(any()) } throws InvalidAccessTokenException()

        assertThat { service.execute(VerifyAccessTokenRequest("any token")) }
            .isFailure()
            .isInstanceOf(InvalidAccessTokenException::class)
    }

    @Test
    fun `throw exception when token has expired`() {
        every { tokenVerifier.verify(any()) } throws ExpiredAccessTokenException()

        assertThat { service.execute(VerifyAccessTokenRequest("any token")) }
            .isFailure()
            .isInstanceOf(ExpiredAccessTokenException::class)
    }

    @Test
    fun `returns user basic information when token is valid`() {
        every { tokenVerifier.verify(any()) } returns UserInformation("any-id", "any-username", "any-mail@gmail.com")

        val response = service.execute(VerifyAccessTokenRequest("any token"))

        assertThat(response).isEqualTo(VerifyAccessTokenResponse("any-id", "any-username", "any-mail@gmail.com"))
    }
}