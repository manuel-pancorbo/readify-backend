package com.readify.api.readerlibrary.controller.getbooksbyids

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.readerlibrary.application.service.getbooksbyids.GetBooksByIdsRequest
import com.readify.readerlibrary.application.service.getbooksbyids.GetBooksByIdsResponse
import com.readify.readerlibrary.application.service.getbooksbyids.GetBooksByIdsService
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

class GetBooksByIdsControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetBooksByIdsService

    @Test
    fun `return ok with requested books`() {
        val serviceRequest = GetBooksByIdsRequest(listOf("1", "2"))
        val serviceResponse = GetBooksByIdsResponse(
            listOf(
                BookResponseMother().inProgressOne("1"),
                BookResponseMother().finishedOne("2")
            )
        )
        every { service.execute(serviceRequest) } returns serviceResponse

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books/?ids=1,2")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("books")).hasSize(2)
        assertThat(httpResponse.getString("books[0].id")).isEqualTo("1")
        assertThat(httpResponse.getString("books[1].id")).isEqualTo("2")
        val firstBook = httpResponse.getMap<String, String>("books[0]")
        val firstBookPrice = httpResponse.getMap<String, String>("books[0].price")
        assertThat(firstBook["id"]).isEqualTo(serviceResponse.books[0].id)
        assertThat(firstBook["author"]).isEqualTo(serviceResponse.books[0].author)
        assertThat(firstBook["title"]).isEqualTo(serviceResponse.books[0].title)
        assertThat(firstBook["cover"]).isEqualTo(serviceResponse.books[0].cover)
        assertThat(firstBook["summary"]).isEqualTo(serviceResponse.books[0].summary)
        assertThat(httpResponse.getList<String>("books[0].tags")).isEqualTo(serviceResponse.books[0].tags)
        assertThat(firstBookPrice["amount"]).isEqualTo(serviceResponse.books[0].priceAmount)
        assertThat(firstBookPrice["currency"]).isEqualTo(serviceResponse.books[0].priceCurrency)
        assertThat(firstBook["completionPercentage"]).isEqualTo(serviceResponse.books[0].completionPercentage)
        assertThat(firstBook["status"]).isEqualTo(serviceResponse.books[0].status.toString().toLowerCase())
        assertThat(firstBook["finishedAt"]).isEqualTo(
            serviceResponse.books[0].finishedAt?.format(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
        )
    }
}