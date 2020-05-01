package com.readify.userprofile.infrastructure.configuration

import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationService
import com.readify.userprofile.domain.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetUserProfilesInformationServiceConfiguration {
    @Bean
    fun getUserProfilesInformationService(userRepository: UserRepository) =
        GetUserProfilesInformationService(userRepository)
}