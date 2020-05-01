package com.readify.userprofile.application.getuserprofileinformation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.userprofile.application.common.UserMother
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetUserProfileInformationServiceShould {
    private val repository: UserRepository = mockk()
    private val service = GetUserProfileInformationService(repository)

    @Test
    fun `return not found when requested user does not exists`() {
        every { repository.findById(UserId(userId)) } returns null

        val response = service.execute(GetUserProfileInformationRequest(userId))

        assertThat(response).isEqualTo(UserNotFound)
    }

    @Test
    fun `return user information when requested user is an existent one`() {
        val user = UserMother().validUser(userId)
        every { repository.findById(UserId(userId)) } returns user

        val response = service.execute(GetUserProfileInformationRequest(userId))

        assertThat(response).isInstanceOf(UserFoundResponse::class)
        response as UserFoundResponse
        assertThat(response.user.id).isEqualTo(user.id.value)
        assertThat(response.user.email).isEqualTo(user.email.value)
        assertThat(response.user.username).isEqualTo(user.username.value)
        assertThat(response.user.fullName).isEqualTo(user.fullName.value)
        assertThat(response.user.image).isEqualTo(user.image?.value)
        assertThat(response.user.website).isEqualTo(user.website?.value)
    }

    companion object {
        private val userId = UUID.randomUUID().toString()
    }
}