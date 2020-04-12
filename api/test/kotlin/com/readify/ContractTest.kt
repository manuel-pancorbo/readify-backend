package com.readify

import com.readify.api.Application
import io.mockk.junit5.MockKExtension
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@ExtendWith(MockKExtension::class)
@ActiveProfiles("contract-test")
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ContractTest {
    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }
}