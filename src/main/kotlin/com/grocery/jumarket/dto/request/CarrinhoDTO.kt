package com.grocery.jumarket.dto.request

data class CarrinhoDTO(
    val carrinhoId: Long,
    val produtoId: Long,
    val quantidade: Long,
    var usuarioId: Long
)