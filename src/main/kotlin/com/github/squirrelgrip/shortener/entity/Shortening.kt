package com.github.squirrelgrip.shortener.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Shortening(
    @field:Id
    val id: String,
    val url: String,
)
