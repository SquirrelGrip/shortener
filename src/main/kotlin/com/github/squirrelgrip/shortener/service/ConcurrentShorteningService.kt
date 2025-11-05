package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.entity.Shortening
import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.util.CodeSupplier

/**
 * When run in a multithreaded environment, using the input URL as the lock.
 * Guarantees that the shortened URL is unique.
 *
 * See README.md for more details about generating URLs
 */
class ConcurrentShorteningService(
    baseUrl: String,
    shorteningRepository: ShorteningRepository,
    codeSupplier: CodeSupplier
) : SimpleShorteningService(
    baseUrl,
    shorteningRepository,
    codeSupplier
) {

    override fun create(url: String): Shortening =
        synchronized(url) {
            super.create(url)
        }

}
