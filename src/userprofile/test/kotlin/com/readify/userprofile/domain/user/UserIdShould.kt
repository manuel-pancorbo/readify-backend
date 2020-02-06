package com.readify.userprofile.domain.user

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class UserIdShould {
    @Test
    fun `throw an exception when value is not a uuid`() {
        assertThat { UserId("any string") }
            .isFailure()
            .isInstanceOf(IllegalArgumentException::class)
    }

    @Test
    fun `not throw an exception when value is a valid uuid`() {
        val userId = UserId("06bfe015-e124-4f2b-b5d9-cb41a39493ef")

        assertThat(userId.value).isEqualTo("06bfe015-e124-4f2b-b5d9-cb41a39493ef")
    }
}