package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.entity.Shortening
import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.util.CodeSupplier
import kotlin.time.ExperimentalTime

/**
 * Use this service when you don't care about concurrency issues and could have multiple shortened URLs for a given input URL.
 *
 * When run in a multithreaded environment, in the event of a race condition in creating a
 * new code for the same URL, 2 or more codes could be generated from the same input URL.
 *
 * See README.md for more details about generating URLs
 */
open class SimpleShorteningService(
    baseUrl: String,
    shorteningRepository: ShorteningRepository,
    codeSupplier: CodeSupplier
) : NonUniqueShorteningService(
    baseUrl,
    shorteningRepository,
    codeSupplier
) {

    @OptIn(ExperimentalTime::class)
    override fun create(url: String): Shortening {
        val shortenings = shorteningRepository.findByUrl(url)
        return if (shortenings.isEmpty()) {
            var newId = codeSupplier.get()
            while (shorteningRepository.existsById(newId)) {
                newId = codeSupplier.get()
            }
            Shortening(newId, url).also {
                shorteningRepository.save(it)
            }
        } else {
            shortenings.first()
        }
    }

}