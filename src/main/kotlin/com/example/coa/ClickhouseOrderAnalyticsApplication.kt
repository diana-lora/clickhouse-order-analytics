package com.example.coa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClickhouseOrderAnalyticsApplication

fun main(args: Array<String>) {
    runApplication<ClickhouseOrderAnalyticsApplication>(*args)
}
