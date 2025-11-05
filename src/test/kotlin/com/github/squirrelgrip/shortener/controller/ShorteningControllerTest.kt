package com.github.squirrelgrip.shortener.controller

import com.github.squirrelgrip.shortener.TestFixtures.CODE
import com.github.squirrelgrip.shortener.TestFixtures.URL
import com.github.squirrelgrip.shortener.exception.ShorteningNotFoundException
import com.github.squirrelgrip.shortener.service.ShorteningService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view


@WebMvcTest(ShorteningController::class) // Replace YourController with your actual controller
@AutoConfigureMockMvc
class ShorteningControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var shorteningService: ShorteningService

    @Test
    fun testRedirectOnGet_GivenCodeExists() {
        given(shorteningService.findUrl(CODE)).willReturn(URL)

        mockMvc.perform(
            get("/abcdef")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(URL))
    }

    @Test
    fun test_GivenNoCode() {
        mockMvc.perform(
            get("/")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("index"))
    }

    @Test
    fun testRedirectOnGet_GivenCodeDoesNotExist() {
        given(shorteningService.findUrl(CODE)).willThrow(ShorteningNotFoundException(CODE))

        mockMvc.perform(
            get("/$CODE")
        )
            .andExpect(status().isNotFound)
            .andExpect(view().name("404"))
    }

    @Test
    fun testCreateOnPost() {
        given(shorteningService.createUrl(CODE)).willThrow(ShorteningNotFoundException(CODE))

        mockMvc.perform(
            post("/").param("url", URL)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("index"))
    }
}