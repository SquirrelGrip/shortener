package com.github.squirrelgrip.shortener.util

import java.util.function.Supplier
import kotlin.random.Random

class CodeSupplier(requiredLength: Int = 6) : Supplier<String> {
    private val letters: Array<String> = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        .toCharArray()
        .map { it.toString() }
        .toTypedArray()

    private val range = 1..requiredLength

    override fun get(): String =
        range.joinToString("") {
            letters[Random.nextInt(0, letters.size)]
        }

}