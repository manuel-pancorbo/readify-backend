package com.readify.authentication.infrastructure.domain.accesstoken

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.readify.authentication.domain.accesstoken.AccessTokenGenerator
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.shared.domain.clock.Clock
import java.util.Date

private const val USERNAME_CLAIM = "username"
private const val EMAIL_CLAIM = "email"
private const val EXPIRATION_DELTA_IN_HOURS = 24L

class JwtAccessTokenGenerator(private val signingSecretKey: String, private val clock: Clock) : AccessTokenGenerator {
    override fun generate(userCredentials: UserCredentials) =
        JWT.create()
            .withIssuer(userCredentials.userId.value)
            .withExpiresAt(Date.from(clock.now().plusHours(EXPIRATION_DELTA_IN_HOURS).toInstant()))
            .withClaim(USERNAME_CLAIM, userCredentials.username.value)
            .withClaim(EMAIL_CLAIM, userCredentials.email.value)
            .sign(Algorithm.HMAC256(signingSecretKey))
            .toString()
}