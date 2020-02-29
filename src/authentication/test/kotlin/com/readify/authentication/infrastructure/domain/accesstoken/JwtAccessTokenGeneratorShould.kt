package com.readify.authentication.infrastructure.domain.accesstoken

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.Username
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

private const val USER_ID = "747ffcd7-67be-4640-84e9-1f80aba1d102"
private const val USERNAME = "manu"
private const val EMAIL = "manuelpancorbo@gmail.com"
private const val PASSWORD = "any encoded password"

class JwtAccessTokenGeneratorShould {
    @Test
    fun `generate a token based on given credentials`() {
        val token = JwtAccessTokenGenerator().generate(
            UserCredentials(
                UserId(USER_ID),
                Username(USERNAME),
                Email(EMAIL),
                EncodedPassword(PASSWORD)
            )
        )
        val now = Date.from(ZonedDateTime.now(ZoneId.of("Europe/Madrid")).toInstant())

        assertThat(token).isNotNull()
        val decodedToken = JWT.decode(token)
        assertThat(decodedToken.issuer).isEqualTo(USER_ID)
        assertThat(decodedToken.username()).isEqualTo(USERNAME)
        assertThat(decodedToken.email()).isEqualTo(EMAIL)
        assertThat(decodedToken.expiresAt).isGreaterThan(now)
    }

    @Test
    fun `ensure that token has been generated with proper algorithm (fail only if an exception is thrown)`() {
        val token = JwtAccessTokenGenerator().generate(
            UserCredentials(
                UserId(USER_ID),
                Username(USERNAME),
                Email(EMAIL),
                EncodedPassword(PASSWORD)
            )
        )

        JWT.require(Algorithm.HMAC256("secret")).build().verify(token)
    }

    private fun DecodedJWT.username() = this.claims["username"]?.asString()
    private fun DecodedJWT.email() = this.claims["email"]?.asString()
}