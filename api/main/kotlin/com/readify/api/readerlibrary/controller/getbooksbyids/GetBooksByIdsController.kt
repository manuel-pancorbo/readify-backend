package com.readify.api.readerlibrary.controller.getbooksbyids

import com.readify.readerlibrary.application.service.getbooksbyids.GetBooksByIdsRequest
import com.readify.readerlibrary.application.service.getbooksbyids.GetBooksByIdsResponse
import com.readify.readerlibrary.application.service.getbooksbyids.GetBooksByIdsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetBooksByIdsController(private val service: GetBooksByIdsService) {
    @GetMapping("/books")
    fun getBook(@RequestParam ids: String): ResponseEntity<out Any> =
        service.execute(GetBooksByIdsRequest(ids.split(",")))
                .toHttpResponse()
}

private fun GetBooksByIdsResponse.toHttpResponse(): ResponseEntity<Any> {
    return ResponseEntity.ok().build()
}
