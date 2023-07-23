package com.grocery.jumarket.dto.view

import com.grocery.jumarket.dto.request.ProdutoDTO
import java.math.BigDecimal

data class ItemCarrinhoViewDTO(
    val id: Long,
    val produto: ProdutoDTO,
    val quantidade: Long,
    val precoUnitario: BigDecimal
)