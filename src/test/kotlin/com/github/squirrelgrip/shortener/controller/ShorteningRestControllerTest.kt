package com.github.squirrelgrip.shortener.controller

import com.github.squirrelgrip.shortener.TestFixtures.CODE
import com.github.squirrelgrip.shortener.TestFixtures.EXPECTED_SHORTENING
import com.github.squirrelgrip.shortener.TestFixtures.URL
import com.github.squirrelgrip.shortener.service.ShorteningService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ShorteningRestControllerTest {
    @Mock
    lateinit var shorteningService: ShorteningService

    lateinit var testSubject: ShorteningRestController

    @BeforeEach
    fun beforeEach() {
        testSubject = ShorteningRestController(shorteningService)
    }

    @Test
    fun create() {
        given(shorteningService.create(URL)).willReturn(EXPECTED_SHORTENING)

        assertThat(testSubject.create(URL)).isEqualTo(EXPECTED_SHORTENING)

        verifyNoMoreInteractions(shorteningService)
    }

    @Test
    fun find() {
        given(shorteningService.find(CODE)).willReturn(EXPECTED_SHORTENING)

        assertThat(testSubject.find(CODE)).isEqualTo(EXPECTED_SHORTENING)

        verifyNoMoreInteractions(shorteningService)
    }

}