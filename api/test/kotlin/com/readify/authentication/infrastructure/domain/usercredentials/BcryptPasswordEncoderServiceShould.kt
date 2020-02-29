package com.readify.authentication.infrastructure.domain.usercredentials

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.readify.authentication.domain.usercredentials.EncodedPassword
import org.junit.jupiter.api.Test

private const val ANY_PASSWORD_PLAIN = "anypassword"

class BcryptPasswordEncoderServiceShould {

    private val service = BcryptPasswordEncoderService()

    @Test
    fun `encrypted password should match with its plain version`() {
        val encryptedPassword = service.encode(ANY_PASSWORD_PLAIN)

        assertThat(service.match(ANY_PASSWORD_PLAIN, encryptedPassword)).isTrue()
    }

    @Test
    fun `a plain password should not match with any random string`() {
        assertThat(
            service.match(
                ANY_PASSWORD_PLAIN,
                EncodedPassword("\$2a\$11\$JFAkuJKGL96vgNEv/tyG7.D.UjWmF4mO683VXIIv1EV7UXcLThd7S")
            )
        ).isFalse()
    }

    @Test
    internal fun test() {
        assertThat(BcryptPasswordEncoderService().encode("something")).isEqualTo(BcryptPasswordEncoderService().encode("something"))
    }
}