package com.grocery.jumarket.dto

import java.math.BigDecimal

data class ItemVendidoDTO(
    val produtoId: Long,
    val quantidade: Long,
    val precoUnitario: BigDecimal
)