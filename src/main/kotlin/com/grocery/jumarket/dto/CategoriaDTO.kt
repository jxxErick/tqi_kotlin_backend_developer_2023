package com.grocery.jumarket.dto

import jakarta.validation.constraints.NotNull

data class CategoriaDTO(
        var id: Long?,
        @field:NotNull(message = "nao pode estar vazio")  var nome: String
)
