package com.grocery.jumarket.dto

import java.math.BigDecimal

data class NewProdutoDTO(
        val nome: String,
        val unidadeDeMedida: String,
        val precoUnitario: BigDecimal,
        val categoriaId: Long
)
