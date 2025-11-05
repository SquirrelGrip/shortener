package com.github.squirrelgrip.shortener.config

import com.github.squirrelgrip.shortener.repository.ShorteningRepository
import com.github.squirrelgrip.shortener.service.ConcurrentShorteningService
import com.github.squirrelgrip.shortener.service.ShorteningService
import com.github.squirrelgrip.shortener.util.CodeSupplier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.github.squirrelgrip.shortener.repository"])
class ShortenerConfig {
    @Value("\${shortener.url:http://localhost:8080}")
    lateinit var baseUrl: String

    @Value("\${shortener.requiredLength:6}")
    var requiredLength: Int = 6

    @Autowired
    lateinit var shorteningRepository: ShorteningRepository

    @Bean
    fun shortenerService(): ShorteningService =
        ConcurrentShorteningService(baseUrl, shorteningRepository, codeSupplier())

    @Bean
    fun codeSupplier(): CodeSupplier =
        CodeSupplier(requiredLength)
}