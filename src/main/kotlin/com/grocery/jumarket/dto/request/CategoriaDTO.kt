package com.grocery.jumarket.dto.request

import jakarta.validation.constraints.NotNull

data class CategoriaDTO(
        val id: Long? = null,
        val nome: String
)