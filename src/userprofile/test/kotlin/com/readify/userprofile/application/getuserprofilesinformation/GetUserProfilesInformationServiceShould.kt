package com.readify.userprofile.application.getuserprofilesinformation

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.readify.userprofile.application.common.UserMother
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetUserProfilesInformationServiceShould {
    private val userRepository: UserRepository = mockk()
    private val service = GetUserProfilesInformationService(userRepository)

    @Test
    fun `return users fetched by repository`() {
        val request = GetUserProfilesInformationRequest(listOf(anUser, anotherUser))
        val users = listOf(UserMother().validUser(anUser), UserMother().validUser(anotherUser))
        every { userRepository.findByIds(listOf(UserId(anUser), UserId(anotherUser))) } returns users

        val response = service.execute(request)

        assertThat(response.users).hasSize(2)
        assertThat(response.users[0].id).isEqualTo(users[0].id.value)
        assertThat(response.users[0].email).isEqualTo(users[0].email.value)
        assertThat(response.users[0].fullName).isEqualTo(users[0].fullName.value)
        assertThat(response.users[0].username).isEqualTo(users[0].username.value)
        assertThat(response.users[0].image).isEqualTo(users[0].image?.value)
        assertThat(response.users[0].website).isEqualTo(users[0].website?.value)
        assertThat(response.users[1].id).isEqualTo(users[1].id.value)
        assertThat(response.users[1].email).isEqualTo(users[1].email.value)
        assertThat(response.users[1].fullName).isEqualTo(users[1].fullName.value)
        assertThat(response.users[1].username).isEqualTo(users[1].username.value)
        assertThat(response.users[1].image).isEqualTo(users[1].image?.value)
        assertThat(response.users[1].website).isEqualTo(users[1].website?.value)
    }

    companion object {
        val anUser = UUID.randomUUID().toString()
        val anotherUser = UUID.randomUUID().toString()
    }
}