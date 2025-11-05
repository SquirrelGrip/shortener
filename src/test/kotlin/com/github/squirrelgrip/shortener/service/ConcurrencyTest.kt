package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.Shortener
import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.util.CodeSupplier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.ExtendWith
import org.opentest4j.AssertionFailedError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = [Shortener::class]
)
class ConcurrencyTest {
    @Autowired
    private lateinit var shorteningRepository: ShorteningRepository

    val executor = Executors.newFixedThreadPool(2)
    val codeSupplier = CodeSupplier(20)

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    fun `Expect concurrency issue when using SimpleShorteningService`() {
        val testSubject = SimpleShorteningService("", shorteningRepository, codeSupplier)
        while (true) {
            // We expect the codes to be the same, but if they are different, it proves we have a concurrency issue
            try {
                testShorteningService(testSubject)
            } catch (_: AssertionFailedError) {
                break
            }
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    fun `Expect concurrency issue when using NonUniqueShortenerService`() {
        val testSubject = NonUniqueShorteningService("", shorteningRepository, codeSupplier)
        while (true) {
            // We expect the codes to be the same, but if they are different, it proves we have a concurrency issue
            try {
                testShorteningService(testSubject)
            } catch (_: AssertionFailedError) {
                break
            }
        }
    }

    @Test
    fun `Expect no concurrency issue when using ConcurrentShorteningService`() {
        val testSubject = ConcurrentShorteningService("", shorteningRepository, codeSupplier)
        var count = 0
        while (true) {
            count++
            testShorteningService(testSubject)
            // 1000 should be enough, but in reality, may need to test a few million times to have confidence, but that takes time
            if (count > 1000) {
                break
            }
        }
    }

   @Test
    fun `Expect no concurrency issue when using FastConcurrentShorteningService`() {
        val testSubject = FastConcurrentShorteningService("", shorteningRepository, codeSupplier)
        var count = 0
        while (true) {
            count++
            testShorteningService(testSubject)
            // 1000 should be enough, but in reality, may need to test a few million times to have confidence, but that takes time
            if (count > 1000) {
                break
            }
        }
    }

    private fun testShorteningService(testSubject: ShorteningService) {
        val url = codeSupplier.get()
        val futures = executor.invokeAll(
            listOf(
                Callable {
                    testSubject.createUrl(url)
                }, Callable {
                    testSubject.createUrl(url)
                }
            )
        )
        assertThat(futures[0].get()).isEqualTo(futures[1].get())
    }
}

