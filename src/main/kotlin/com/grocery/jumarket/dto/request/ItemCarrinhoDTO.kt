package com.grocery.jumarket.dto.request

import java.math.BigDecimal

data class ItemCarrinhoDTO(
    val id: Long,
    val produto: ProdutoDTO,
    val quantidade: Long,
    val precoUnitario: BigDecimal
)