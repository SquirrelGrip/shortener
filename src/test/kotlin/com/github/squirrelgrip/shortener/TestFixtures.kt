package com.github.squirrelgrip.shortener

import com.github.squirrelgrip.shortener.entity.Shortening

object TestFixtures {
    const val BASE_URL = "http://short.ly/"
    const val URL = "https://www.google.com"
    const val CODE = "abcdef"
    const val CODE2 = "uvwxyz"
    val EXPECTED_SHORTENING = Shortening(CODE, URL)
}