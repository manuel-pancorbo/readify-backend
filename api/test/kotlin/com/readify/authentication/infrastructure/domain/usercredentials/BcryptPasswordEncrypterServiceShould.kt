package com.readify.authentication.infrastructure.domain.usercredentials

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.readify.authentication.domain.usercredentials.EncryptedPassword
import org.junit.jupiter.api.Test

private const val ANY_PASSWORD_PLAIN = "anypassword"

class BcryptPasswordEncrypterServiceShould {

    private val service = BcryptPasswordEncrypterService()

    @Test
    fun `encrypted password should match with its plain version`() {
        val encryptedPassword = service.encrypt(ANY_PASSWORD_PLAIN)

        assertThat(service.match(ANY_PASSWORD_PLAIN, encryptedPassword)).isTrue()
    }

    @Test
    fun `a plain password should not match with any random string`() {
        assertThat(
            service.match(
                ANY_PASSWORD_PLAIN,
                EncryptedPassword("\$2a\$11\$JFAkuJKGL96vgNEv/tyG7.D.UjWmF4mO683VXIIv1EV7UXcLThd7S")
            )
        ).isFalse()
    }
}