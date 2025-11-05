package com.github.squirrelgrip.shortener.service

import com.github.squirrelgrip.shortener.entity.Shortening
import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.util.CodeSupplier

/**
 * Use this service when you care about threading issues.
 *
 * When run in a multithreaded environment, the double check lock prevents 2 threads creating
 * new codes for the same URL. When the first thread asks for the new code, all other threads are blocked, using the url as a lock
 * until the thread has generated and saved the code to the database.
 *
 * The further threads that were block will repeat the lookup in the database and return the value created by the first thread.
 *
 * See README.md for more details about generating URLs
 */
class FastConcurrentShorteningService(
    baseUrl: String,
    shorteningRepository: ShorteningRepository,
    codeSupplier: CodeSupplier
) : SimpleShorteningService(
    baseUrl,
    shorteningRepository,
    codeSupplier
) {

    override fun create(url: String): Shortening =
        shorteningRepository.findByUrl(url).let {
            if (it.isEmpty()) {
                synchronized(url) {
                    super.create(url)
                }
            } else {
                it.first()
            }
        }

}