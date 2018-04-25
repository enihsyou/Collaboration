package com.enihsyou.collaboration.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class BasicController {

    @GetMapping("/hello")
    fun sayHelloWorld(): Mono<String> {
        return Mono.just("Hello World")
    }
}
