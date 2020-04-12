package com.readify.api.search.controller

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.application.service.searchbooks.SearchBooksRequest
import com.readify.application.service.searchbooks.SearchBooksResponse
import com.readify.application.service.searchbooks.SearchBooksService
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.UUID

class GetSearchBooksControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: SearchBooksService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `return 200 with empty list when no books have been found`() {
        val serviceRequest = SearchBooksRequest(text = "no-matching-query")
        every { service.execute(serviceRequest) } returns SearchBooksResponse(0, emptyList())

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books?text=no-matching-query")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("results")).hasSize(0)
        assertThat(httpResponse.getInt("total")).isEqualTo(0)
    }

    @Test
    fun `return 200 with found books`() {
        val authorId = UUID.randomUUID().toString()
        val serviceRequest = SearchBooksRequest(text = "harry", tag = "fantasy", authorId = authorId)
        val serviceResponse = SearchBooksResponse(1, listOf(ApplicationBookMother().finishedOne()))
        every { service.execute(serviceRequest) } returns serviceResponse

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books?text=harry&tag=fantasy&author=$authorId")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        val results = httpResponse.getList<String>("results")
        assertThat(results).hasSize(1)
        val firstResult = httpResponse.getMap<String, String>("results[0]")
        val firstResultPrice = httpResponse.getMap<String, String>("results[0].price")
        val firstResultTags = httpResponse.getList<String>("results[0].tags")
        assertThat(httpResponse.getInt("total")).isEqualTo(1)
        assertThat(firstResult["id"]).isEqualTo(serviceResponse.results[0].bookId)
        assertThat(firstResult["authorId"]).isEqualTo(serviceResponse.results[0].authorId)
        assertThat(firstResult["title"]).isEqualTo(serviceResponse.results[0].title)
        assertThat(firstResult["summary"]).isEqualTo(serviceResponse.results[0].summary)
        assertThat(firstResult["cover"]).isEqualTo(serviceResponse.results[0].cover)
        assertThat(firstResult["status"]).isEqualTo(serviceResponse.results[0].status.toString().toLowerCase())
        assertThat(firstResult["completionPercentage"]).isEqualTo(serviceResponse.results[0].completionPercentage)
        assertThat(firstResult["finishedAt"]).isEqualTo(serviceResponse.results[0].finishedAt
            ?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        assertThat(firstResultPrice["amount"]).isEqualTo(serviceResponse.results[0].priceAmount)
        assertThat(firstResultPrice["currency"]).isEqualTo(serviceResponse.results[0].priceCurrency)
        assertThat(firstResultTags).isEqualTo(serviceResponse.results[0].tags)
    }
}