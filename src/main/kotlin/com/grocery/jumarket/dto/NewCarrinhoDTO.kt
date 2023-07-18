package com.grocery.jumarket.dto

data class NewCarrinhoDTO(
    val carrinhoId: Long,
    val produtos: List<NewProdutoDTO>,
    val precoTotal: Double
)