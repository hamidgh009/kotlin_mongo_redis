package com.example.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching


@SpringBootApplication
@EnableCaching
class KotlinApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinApplication::class.java, *args)
}
