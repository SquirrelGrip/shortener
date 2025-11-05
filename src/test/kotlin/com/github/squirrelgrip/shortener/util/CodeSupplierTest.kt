package com.github.squirrelgrip.shortener.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CodeSupplierTest {
    @Test
    fun get_ShouldReturnStringOfRequiredLength() {
        val testSubject = CodeSupplier(requiredLength = 10)
        val set = mutableSetOf<String>()
        (1..100).forEach { _ ->
            val code = testSubject.get()
            assertThat(code).hasSize(10)
            set.add(code)
        }
        assertThat(set).hasSize(100) // This can fail, but the chances are 100 in 62^10, which is 1 in 8.3929937e+15! Quite e a low chance
    }
}