package com.grocery.jumarket.dto

data class NewProdutoDTO(
        val nome: String,
        val unidadeDeMedida: String,
        val precoUnitario: Double,
        val categoriaId: Long
)
