package com.readify.authentication.infrastructure.domain.accesstoken

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.readify.authentication.domain.accesstoken.AccessTokenGenerator
import com.readify.authentication.domain.usercredentials.UserCredentials
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class JwtAccessTokenGenerator : AccessTokenGenerator {
    override fun generate(userCredentials: UserCredentials) =
        JWT.create()
            .withIssuer(userCredentials.userId.value)
            .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.of("Europe/Madrid")).plusHours(24).toInstant()))
            .withClaim("username", userCredentials.username.value)
            .withClaim("email", userCredentials.email.value)
            .sign(Algorithm.HMAC256("secret"))
            .toString()
}