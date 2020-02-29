package com.readify.authentication.infrastructure.domain.accesstoken

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.readify.authentication.domain.accesstoken.AccessTokenGenerator
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.shared.domain.clock.Clock
import java.util.Date

class JwtAccessTokenGenerator(private val clock: Clock) : AccessTokenGenerator {
    override fun generate(userCredentials: UserCredentials) =
        JWT.create()
            .withIssuer(userCredentials.userId.value)
            .withExpiresAt(Date.from(clock.now().plusHours(24).toInstant()))
            .withClaim("username", userCredentials.username.value)
            .withClaim("email", userCredentials.email.value)
            .sign(Algorithm.HMAC256("secret"))
            .toString()
}