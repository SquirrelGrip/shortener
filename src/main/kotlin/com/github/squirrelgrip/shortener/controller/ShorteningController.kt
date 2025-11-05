package com.github.squirrelgrip.shortener.controller

import com.github.squirrelgrip.shortener.form.ShorteningForm
import com.github.squirrelgrip.shortener.service.ShorteningService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.view.InternalResourceViewResolver


@Controller
@EnableWebMvc
class ShorteningController(
    private val shorteningService: ShorteningService
) {
    @GetMapping("/")
    fun index(): String =
        "index"

    @GetMapping("/{code:^[A-Za-z0-9]+$}")
    fun redirect(@PathVariable("code") code: String): String =
        "redirect:${shorteningService.findUrl(code)}"

    @PostMapping("/")
    fun create(model: Model, @ModelAttribute shorteningForm: ShorteningForm): String {
        val url = shorteningForm.url
        if (!url.isNullOrBlank()) {
            model.addAttribute("shortenedUrl", shorteningService.createUrl(url))
        }
        return "index"
    }

    @Bean
    fun getViewResolver(): ViewResolver {
        val resolver = InternalResourceViewResolver()
        resolver.setPrefix("/")
        resolver.setSuffix(".html")
        return resolver
    }

}