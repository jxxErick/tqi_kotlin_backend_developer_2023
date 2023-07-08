package com.grocery.jumarket.exception

import java.time.LocalDateTime

data class ExceptionDetails(
        val title: String,
        val timestamp: LocalDateTime,
        val status: Int,
        val exception: String,
        var details: MutableMap<String, String?>
)


