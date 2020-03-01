package com.readify.authentication.infrastructure.domain.accesstoken

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.readify.authentication.domain.accesstoken.AccessTokenVerifier
import com.readify.authentication.domain.accesstoken.ExpiredAccessTokenException
import com.readify.authentication.domain.accesstoken.InvalidAccessTokenException
import com.readify.authentication.domain.accesstoken.UserInformation

private const val USERNAME_CLAIM = "username"
private const val EMAIL_CLAIM = "email"

class JwtAccessTokenVerifier(private val signingSecretKey: String) : AccessTokenVerifier {
    override fun verify(accessToken: String) =
        try {
            JWT.require(Algorithm.HMAC256(signingSecretKey)).build().verify(accessToken).toUserInformation()
        } catch (exception: SignatureVerificationException) {
            throw InvalidAccessTokenException()
        } catch (exception: TokenExpiredException) {
            throw ExpiredAccessTokenException()
        }
}

private fun DecodedJWT.toUserInformation() = UserInformation(this.issuer, this.username(), this.email())
private fun DecodedJWT.username() = this.claims[USERNAME_CLAIM]?.asString() ?: throw InvalidAccessTokenException()
private fun DecodedJWT.email() = this.claims[EMAIL_CLAIM]?.asString() ?: throw InvalidAccessTokenException()
