package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.TestFixtures.BASE_URL
import com.github.squirrelgrip.shortener.TestFixtures.URL
import com.github.squirrelgrip.shortener.TestFixtures.CODE
import com.github.squirrelgrip.shortener.TestFixtures.CODE2
import com.github.squirrelgrip.shortener.TestFixtures.EXPECTED_SHORTENING
import com.github.squirrelgrip.shortener.entity.Shortening
import com.github.squirrelgrip.shortener.exception.ShorteningNotFoundException
import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.util.CodeSupplier
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class FastConcurrentShorteningServiceTest {

    @Mock
    lateinit var shorteningRepository: ShorteningRepository

    @Mock
    lateinit var codeSupplier: CodeSupplier

    lateinit var testSubject: FastConcurrentShorteningService

    @BeforeEach
    fun beforeEach() {
        testSubject = FastConcurrentShorteningService(BASE_URL, shorteningRepository, codeSupplier)
    }

    @Test
    fun create_GivenUrlDoesNotExist() {
        given(codeSupplier.get()).willReturn(CODE)
        given(shorteningRepository.findByUrl(URL)).willReturn(emptyList())
        given(shorteningRepository.existsById(CODE)).willReturn(false)

        assertEquals(EXPECTED_SHORTENING, testSubject.create(URL))

        verify(shorteningRepository).save(EXPECTED_SHORTENING)
    }

    @Test
    fun create_GivenUrlDoesNotExistAndCodeAlreadyInUse() {
        given(codeSupplier.get()).willReturn(CODE2).willReturn(CODE)
        given(shorteningRepository.findByUrl(URL)).willReturn(emptyList())
        given(shorteningRepository.existsById(CODE2)).willReturn(true)
        given(shorteningRepository.existsById(CODE)).willReturn(false)

        assertEquals(EXPECTED_SHORTENING, testSubject.create(URL))

        verify(shorteningRepository).save(EXPECTED_SHORTENING)
    }

    @Test
    fun create_GivenUrlAlreadyCreated() {
        given(shorteningRepository.findByUrl(URL)).willReturn(listOf(Shortening(CODE, URL)))

        assertEquals(Shortening(CODE, URL), testSubject.create(URL))

        verifyNoInteractions(codeSupplier)
    }

    @Test
    fun createUrl_GivenUrlDoesNotExist() {
        given(codeSupplier.get()).willReturn(CODE)
        given(shorteningRepository.findByUrl(URL)).willReturn(emptyList())
        given(shorteningRepository.existsById(CODE)).willReturn(false)

        assertEquals("$BASE_URL$CODE", testSubject.createUrl(URL))

        verify(shorteningRepository).save(EXPECTED_SHORTENING)
    }

    @Test
    fun createUrl_GivenUrlAlreadyCreated() {
        given(shorteningRepository.findByUrl(URL)).willReturn(listOf(EXPECTED_SHORTENING))

        assertEquals("$BASE_URL$CODE", testSubject.createUrl(URL))

        verifyNoInteractions(codeSupplier)
    }

    @Test
    fun find_GivenUrlExists() {
        given(shorteningRepository.findById(CODE)).willReturn(Optional.of(EXPECTED_SHORTENING))

        assertEquals(EXPECTED_SHORTENING, testSubject.find(CODE))
    }

    @Test
    fun find_GivenUrlDoesNotExist() {
        given(shorteningRepository.findById(CODE)).willReturn(Optional.empty())

        assertThatThrownBy{ testSubject.find(CODE) }.isInstanceOf(ShorteningNotFoundException::class.java).hasMessage("Shortened URL not found for abcdef")
    }

     @Test
    fun findUrl_GivenUrlExists() {
        given(shorteningRepository.findById(CODE)).willReturn(Optional.of(EXPECTED_SHORTENING))

        assertThat(testSubject.findUrl(CODE)).isEqualTo(URL)
    }

    @Test
    fun findUrl_GivenUrlDoesNotExist() {
        given(shorteningRepository.findById(CODE)).willReturn(Optional.empty())

        assertThatThrownBy{ testSubject.findUrl(CODE) }.isInstanceOf(ShorteningNotFoundException::class.java).hasMessage("Shortened URL not found for abcdef")
    }

}