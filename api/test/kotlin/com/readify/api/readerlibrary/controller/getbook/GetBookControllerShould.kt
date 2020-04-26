package com.readify.api.readerlibrary.controller.getbook

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.readerlibrary.application.service.getbook.BookNotFound
import com.readify.readerlibrary.application.service.getbook.GetBookRequest
import com.readify.readerlibrary.application.service.getbook.GetBookService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.UUID

class GetBookControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetBookService

    @Test
    fun `return 404 when book does not exists`() {
        val serviceRequest = GetBookRequest(bookId)
        every { service.execute(serviceRequest) } returns BookNotFound

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/books/$bookId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok with requested book`() {
        val serviceRequest = GetBookRequest(bookId)
        val serviceResponse = GetBookResponseMother().createFinishedBook(bookId)
        every { service.execute(serviceRequest) } returns serviceResponse

        val response = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/books/$bookId")
            .then()
            .statusCode(200)
            .body("id", equalTo(bookId))
            .extract()
            .response()
            .jsonPath()

        val bookPrice = response.getMap<String, String>("price")
        assertThat(response.getString("id")).isEqualTo(serviceResponse.id)
        assertThat(response.getString("authorId")).isEqualTo(serviceResponse.authorId)
        assertThat(response.getString("cover")).isEqualTo(serviceResponse.cover)
        assertThat(response.getString("title")).isEqualTo(serviceResponse.title)
        assertThat(response.getString("summary")).isEqualTo(serviceResponse.summary)
        assertThat(response.getString("status")).isEqualTo(serviceResponse.status.toString().toLowerCase())
        assertThat(response.getInt("completionPercentage")).isEqualTo(serviceResponse.completionPercentage)
        assertThat(bookPrice["amount"]).isEqualTo(serviceResponse.priceAmount)
        assertThat(bookPrice["currency"]).isEqualTo(serviceResponse.priceCurrency)
        assertThat(response.getList<String>("tags")).isEqualTo(serviceResponse.tags)
        assertThat(response.getString("finishedAt")).isEqualTo(serviceResponse.finishedAt?.format(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        assertThat(response.getList<Map<String, String>>("chapters")).hasSize(1)
        val chapters = response.getList<Map<String, String>>("chapters")
        val chapterPrice = response.getMap<String, String>("chapters[0].price")
        assertThat(chapters[0]["id"]).isEqualTo(serviceResponse.chapters[0].id)
        assertThat(chapters[0]["title"]).isEqualTo(serviceResponse.chapters[0].title)
        assertThat(chapters[0]["order"]).isEqualTo(serviceResponse.chapters[0].order)
        assertThat(chapters[0]["excerpt"]).isEqualTo(serviceResponse.chapters[0].excerpt)
        assertThat(chapterPrice["amount"]).isEqualTo(serviceResponse.chapters[0].priceAmount)
        assertThat(chapterPrice["currency"]).isEqualTo(serviceResponse.chapters[0].priceCurrency)
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
    }
}