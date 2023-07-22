package com.grocery.jumarket.dto.request

import jakarta.validation.constraints.NotNull

data class NewCategoriaDTO(@field:NotNull(message = "nao pode estar vazio")var nome: String)