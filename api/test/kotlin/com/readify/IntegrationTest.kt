package com.readify

import com.readify.api.Application
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import java.io.File

@ExtendWith(MockKExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {
    companion object {
        @Container
        val docker: Unit = KotlinDockerComposeContainer(File("docker-compose.yml"))
            .withLocalCompose(true)
            .withExposedService("postgresql", 5432, Wait.defaultWaitStrategy())
            .withExposedService("elasticsearch", 9200, Wait.defaultWaitStrategy())
            .start()
    }
}

class KotlinDockerComposeContainer(vararg files: File) : DockerComposeContainer<KotlinDockerComposeContainer>(*files)