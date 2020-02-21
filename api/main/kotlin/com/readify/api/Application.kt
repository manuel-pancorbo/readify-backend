package com.readify.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [
    "com.readify.api",
    "com.readify.userprofile.infrastructure",
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