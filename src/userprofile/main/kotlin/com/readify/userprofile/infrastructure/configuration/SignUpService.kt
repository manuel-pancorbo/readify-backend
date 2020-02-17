package com.readify.userprofile.infrastructure.configuration

import com.readify.userprofile.application.SignUpService
import com.readify.userprofile.domain.user.UserFactory
import com.readify.userprofile.infrastructure.jpa.user.JpaUserRepository
import com.readify.userprofile.infrastructure.jpa.user.JpaUserDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SignUpServiceConfiguration {
    @Bean
    fun signUpService(userFactory: UserFactory, userRepository: JpaUserRepository) =
        SignUpService(userFactory, userRepository)

    @Bean
    fun jdbcUserRepository(jpaJpaUserDataSource: JpaUserDataSource) =
        JpaUserRepository(jpaJpaUserDataSource)

    @Bean fun userFactory(userRepository: JpaUserRepository) =
        UserFactory(userRepository)
}