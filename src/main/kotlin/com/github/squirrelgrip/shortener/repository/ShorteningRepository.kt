package com.github.squirrelgrip.shortener.repository

import com.github.squirrelgrip.shortener.entity.Shortening
import org.springframework.data.jpa.repository.JpaRepository

interface ShorteningRepository: JpaRepository<Shortening, String> {
    fun findByUrl(url: String): List<Shortening>
}