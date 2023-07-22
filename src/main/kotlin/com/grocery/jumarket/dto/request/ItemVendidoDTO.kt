package com.grocery.jumarket.dto.request

import java.math.BigDecimal

data class ItemVendidoDTO(
    val produtoId: Long,
    val quantidade: Long,
    val precoUnitario: BigDecimal
)