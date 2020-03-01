package com.readify.authentication.infrastructure.domain.accesstoken

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.readify.authentication.domain.accesstoken.ExpiredAccessTokenException
import com.readify.authentication.domain.accesstoken.InvalidAccessTokenException
import com.readify.authentication.domain.accesstoken.UserInformation
import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.Username
import com.readify.shared.domain.clock.Clock
import org.junit.jupiter.api.Test
import java.util.Date

private const val SIGNING_SECRET_KEY = "any-secret"
private const val INVALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gR" +
        "G9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.xl3D3Wk5HRk3lWiY4SDevgB2dX_zzZDgDT4jiEDHsK4"

class JwtAccessTokenVerifierShould {

    private val tokenVerifier = JwtAccessTokenVerifier(SIGNING_SECRET_KEY)

    @Test
    fun `throw exception when token is invalid`() {
        assertThat { tokenVerifier.verify(INVALID_TOKEN) }
            .isFailure()
            .isInstanceOf(InvalidAccessTokenException::class)
    }

    @Test
    fun `throw exception when token is expired`() {
        assertThat { tokenVerifier.verify(expiredToken()) }
            .isFailure()
            .isInstanceOf(ExpiredAccessTokenException::class)
    }

    @Test
    fun `return user information for valid access token`() {
        val userCredentials = UserCredentials(
            UserId("any-user-id"),
            Username("foobar"),
            Email("foobar@gmail.com"),
            EncodedPassword("any-encoded-password")
        )
        val validToken = JwtAccessTokenGenerator(SIGNING_SECRET_KEY, Clock()).generate(userCredentials)

        val userInformation = tokenVerifier.verify(validToken)

        assertThat(userInformation).isEqualTo(UserInformation("any-user-id", "foobar", "foobar@gmail.com"))
    }

    private fun expiredToken() =
        JWT.create()
            .withExpiresAt(Date.from(Clock().now().minusSeconds(5).toInstant()))
            .withIssuer("any-user-id")
            .sign(Algorithm.HMAC256(SIGNING_SECRET_KEY))
            .toString()
}