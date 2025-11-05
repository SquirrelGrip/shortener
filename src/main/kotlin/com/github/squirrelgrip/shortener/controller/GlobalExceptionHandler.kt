package com.github.squirrelgrip.shortener.controller

import com.github.squirrelgrip.shortener.exception.ShorteningNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ShorteningNotFoundException::class)
    fun handleShorteningNotFoundException(): String {
        return "404"
    }
}