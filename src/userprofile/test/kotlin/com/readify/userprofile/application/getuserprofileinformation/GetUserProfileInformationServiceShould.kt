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

        assertThat(response).isInstanceOf(UserInformationResponse::class)
        response as UserInformationResponse
        assertThat(response.id).isEqualTo(user.id.value)
        assertThat(response.email).isEqualTo(user.email.value)
        assertThat(response.username).isEqualTo(user.username.value)
        assertThat(response.fullName).isEqualTo(user.fullName.value)
        assertThat(response.image).isEqualTo(user.image?.value)
        assertThat(response.website).isEqualTo(user.website?.value)
    }

    companion object {
        private val userId = UUID.randomUUID().toString()
    }
}