package com.readify.authentication.infrastructure.springboot

import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.authentication.domain.accesstoken.ExpiredAccessTokenException
import com.readify.authentication.domain.accesstoken.InvalidAccessTokenException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val AUTHORIZATION_HEADER = "Authorization"

class HeaderRequesterArgumentResolver(private val tokenVerifierService: VerifyAccessTokenService) :
    HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == Requester::class.java

    override fun resolveArgument(
        parameter: MethodParameter?,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ) =
        try {
            when (val accessToken = webRequest.getHeader(AUTHORIZATION_HEADER)?.extractAccessToken()) {
                null -> AnonymousUser
                else -> tokenVerifierService.execute(VerifyAccessTokenRequest(accessToken)).toLoggedUserRequester()
            }
        } catch (exception: Throwable) {
            handleExceptions(exception)
        }

    private fun handleExceptions(exception: Throwable): AnonymousUser {
        return when (exception) {
            is InvalidAccessTokenException, is ExpiredAccessTokenException -> AnonymousUser
            else -> throw exception
        }
    }
}

private fun String.extractAccessToken() = this.replace("Bearer ", "")
private fun VerifyAccessTokenResponse.toLoggedUserRequester() = LoggedUser(this.userId, this.username, this.email)
