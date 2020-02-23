package com.readify.authentication.infrastructure.eventsubscriber

import com.ninjasquad.springmockk.MockkBean
import com.readify.api.Application
import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsRequest
import com.readify.authentication.application.service.usercredentials.CreateUserCredentialsService
import com.readify.shared.domain.event.bus.EventBus
import com.readify.shared.domain.event.user.PlainUserCredentialsCreated
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(MockKExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlainUserCredentialsCreatedSubscribeShould {
    @MockkBean(relaxed = true)
    private lateinit var service: CreateUserCredentialsService

    @Autowired
    private lateinit var eventBus: EventBus

    @Test
    fun `execute application service when  domain event is published`() {
        val domainEvent = PlainUserCredentialsCreated("any-id", "manu", "manuelpancorbo90@gmail.com", "anypassword")

        eventBus.publish(listOf(domainEvent))

        val request = CreateUserCredentialsRequest("any-id", "manu", "manuelpancorbo90@gmail.com", "anypassword")
        verify { service.execute(request) }
    }
}