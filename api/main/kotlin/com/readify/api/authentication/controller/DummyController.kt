package com.readify.api.authentication.controller

import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DummyController {
    @GetMapping("/dummy")
    fun generateToken(requester: Requester): ResponseEntity<String> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            is LoggedUser -> ResponseEntity.ok("ooooooll ray!")
        }
}