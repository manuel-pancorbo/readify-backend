package com.readify.userprofile.infrastructure.configuration

import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationService
import com.readify.userprofile.domain.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetUserProfileInformationServiceConfiguration {
    @Bean
    fun getUserProfileInformationService(repository: UserRepository) = GetUserProfileInformationService(repository)
}