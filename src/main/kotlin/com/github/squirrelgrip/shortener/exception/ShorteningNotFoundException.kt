package com.github.squirrelgrip.shortener.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Shortened URL")
class ShorteningNotFoundException(code: String): RuntimeException("Shortened URL not found for $code")
