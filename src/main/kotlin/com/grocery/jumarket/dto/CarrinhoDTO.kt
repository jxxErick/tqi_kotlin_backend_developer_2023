package com.grocery.jumarket.dto

data class CarrinhoDTO(
    val carrinhoId: Long,
    val produtoId: Long,
    val quantidade: Long,
    var usuarioId: Long
)