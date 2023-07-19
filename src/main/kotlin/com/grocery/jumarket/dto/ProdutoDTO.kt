package com.grocery.jumarket.dto

import java.math.BigDecimal

data class ProdutoDTO(
        val id: Long?,
        val nome: String,
        val unidadeDeMedida: String,
        val precoUnitario: BigDecimal,
        val categoriaId: Long?
)
