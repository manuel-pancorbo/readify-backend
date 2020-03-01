package com.readify.authentication.infrastructure.springboot

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.accesstoken.ExpiredAccessTokenException
import com.readify.authentication.domain.accesstoken.InvalidAccessTokenException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.web.context.request.NativeWebRequest

class HeaderRequesterArgumentResolverShould {

    private val tokenVerifierService: VerifyAccessTokenService = mockk()
    private val nativeWebRequest: NativeWebRequest = mockk()
    private val resolver = HeaderRequesterArgumentResolver(tokenVerifierService)

    @Test
    fun `returns anonymous requester when access token is not present`() {
        every { nativeWebRequest.getHeader("Authorization") } returns null

        val requester = resolver.resolveArgument(null, null, nativeWebRequest, null)

        assertThat(requester).isEqualTo(AnonymousUser)
    }

    @Test
    fun `returns anonymous requester when access token is present but invalid`() {
        every { nativeWebRequest.getHeader("Authorization") } returns "Bearer any-token"
        every { tokenVerifierService.execute(VerifyAccessTokenRequest("any-token")) }
            .throws(InvalidAccessTokenException())

        val requester = resolver.resolveArgument(null, null, nativeWebRequest, null)

        assertThat(requester).isEqualTo(AnonymousUser)
    }

    @Test
    fun `returns anonymous requester when access token is present but expired`() {
        every { nativeWebRequest.getHeader("Authorization") } returns "Bearer any-token"
        every { tokenVerifierService.execute(VerifyAccessTokenRequest("any-token")) }
            .throws(ExpiredAccessTokenException())

        val requester = resolver.resolveArgument(null, null, nativeWebRequest, null)

        assertThat(requester).isEqualTo(AnonymousUser)
    }

    @Test
    fun `returns logged requester when access token is present and valid`() {
        every { nativeWebRequest.getHeader("Authorization") } returns "Bearer any-token"
        every { tokenVerifierService.execute(VerifyAccessTokenRequest("any-token")) }
            .returns(VerifyAccessTokenResponse("any-id", "foobar", "foobar@gmail.com"))

        val requester = resolver.resolveArgument(null, null, nativeWebRequest, null)

        assertThat(requester).isEqualTo(LoggedUser("any-id", "foobar", "foobar@gmail.com"))
    }
}