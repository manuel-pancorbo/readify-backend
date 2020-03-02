package com.readify.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@SpringBootApplication(scanBasePackages = [
    "com.readify.api",
    "com.readify.userprofile.infrastructure",
    "com.readify.authentication.infrastructure",
    "com.readify.bookpublishing.infrastructure",
    "com.readify.shared.infrastructure"
])
class Application {
    companion object {
        @JvmStatic
        @Suppress("SpreadOperator")
        fun main(args: Array<String>) {
            runApplication<Application>(*args)
        }
    }
}