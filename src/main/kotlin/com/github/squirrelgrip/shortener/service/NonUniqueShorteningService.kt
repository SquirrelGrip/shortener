package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.entity.Shortening
import com.github.squirrelgrip.shortener.exception.ShorteningNotFoundException
import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.util.CodeSupplier
import kotlin.time.ExperimentalTime

/**
 * Use this service when you don't care about a unique shortened URL.
 *
 * See README.md for more details about generating URLs
 */
open class NonUniqueShorteningService(
    override val baseUrl: String,
    val shorteningRepository: ShorteningRepository,
    val codeSupplier: CodeSupplier
): ShorteningService {

    @OptIn(ExperimentalTime::class)
    override fun create(url: String): Shortening {
        var newId = codeSupplier.get()
        while (shorteningRepository.existsById(newId)) {
            newId = codeSupplier.get()
        }
        return shorteningRepository.save(Shortening(newId, url))
    }

    override fun find(code: String): Shortening =
        shorteningRepository.findById(code).orElseThrow { ShorteningNotFoundException(code) }

}