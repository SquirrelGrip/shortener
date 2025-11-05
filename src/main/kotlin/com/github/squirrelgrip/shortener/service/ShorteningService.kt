package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.entity.Shortening

interface ShorteningService {
    fun create(url: String): Shortening

    fun createUrl(url: String): String =
        "$baseUrl${create(url).id}"

    fun find(code: String): Shortening

    fun findUrl(code: String): String =
        find(code).url

    val baseUrl: String
}