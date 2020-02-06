package com.readify.api.userprofile.configuration

import com.readify.userprofile.application.SignUpService
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SignUpConfiguration {
    @Bean
    fun signUpService() = SignUpService(
        object: UserRepository {
            override fun save(user: User): User {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun findByUsername(username: String): User? {
                return null
            }

            override fun findByEmail(email: String): User? {
                return null
            }

        }
    )
}