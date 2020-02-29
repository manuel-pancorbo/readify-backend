package com.readify.authentication.infrastructure.domain.accesstoken

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import assertk.assertions.isNotNull
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.Username
import com.readify.shared.domain.clock.Clock
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.Date

private const val USER_ID = "747ffcd7-67be-4640-84e9-1f80aba1d102"
private const val USERNAME = "manu"
private const val EMAIL = "manuelpancorbo@gmail.com"
private const val PASSWORD = "any encoded password"
private const val SIGNING_SECRET_KEY = "secretkey"

class JwtAccessTokenGeneratorShould {
    private val clock: Clock = mockk()
    private val jwtAccessTokenGenerator = JwtAccessTokenGenerator(SIGNING_SECRET_KEY, clock)

    @Test
    fun `generate a token based on given credentials`() {
        val now = ZonedDateTime.now()
        every { clock.now() } returns now
        val token = jwtAccessTokenGenerator.generate(
            UserCredentials(
                UserId(USER_ID),
                Username(USERNAME),
                Email(EMAIL),
                EncodedPassword(PASSWORD)
            )
        )

        assertThat(token).isNotNull()
        val decodedToken = JWT.decode(token)
        assertThat(decodedToken.issuer).isEqualTo(USER_ID)
        assertThat(decodedToken.username()).isEqualTo(USERNAME)
        assertThat(decodedToken.email()).isEqualTo(EMAIL)
        assertThat(decodedToken.expiresAt).isGreaterThan(now.plusHours(23).toDate())
        assertThat(decodedToken.expiresAt).isLessThan(now.plusHours(25).toDate())
    }

    @Test
    fun `ensure that token has been generated with proper algorithm (fail only if an exception is thrown)`() {
        every { clock.now() } returns ZonedDateTime.now()

        val token = jwtAccessTokenGenerator.generate(
            UserCredentials(
                UserId(USER_ID),
                Username(USERNAME),
                Email(EMAIL),
                EncodedPassword(PASSWORD)
            )
        )

        JWT.require(Algorithm.HMAC256(SIGNING_SECRET_KEY)).build().verify(token)
    }

    private fun DecodedJWT.username() = this.claims["username"]?.asString()
    private fun DecodedJWT.email() = this.claims["email"]?.asString()
    private fun ZonedDateTime.toDate() = this.toInstant().let { Date.from(it) }
}