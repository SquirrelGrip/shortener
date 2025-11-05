package com.github.squirrelgrip.shortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Shortener

fun main(args: Array<String>) {
    runApplication<Shortener>(*args)
}