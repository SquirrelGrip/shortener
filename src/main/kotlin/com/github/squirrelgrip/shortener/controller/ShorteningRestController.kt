package com.github.squirrelgrip.shortener.controller

import com.github.squirrelgrip.shortener.entity.Shortening
import com.github.squirrelgrip.shortener.service.ShorteningService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShorteningRestController(
    private val shorteningService: ShorteningService
) {
    @PostMapping("/api/create")
    fun create(@RequestBody url: String): Shortening =
        shorteningService.create(url)

    @PostMapping("/api/find/{code}")
    fun find(@PathVariable("code") code: String): Shortening? =
        shorteningService.find(code)
}