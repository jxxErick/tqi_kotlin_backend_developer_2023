package com.grocery.jumarket.dto

import com.grocery.jumarket.ennumeration.FormaDePagamento
import java.math.BigDecimal

data class VendaDTO(
    val id: Long,
    val usuario: UsuarioDTO,
    val valorTotal: BigDecimal,
    val formaDePagamento: String,
    val itensVendidos: List<ItemVendidoDTO>
)