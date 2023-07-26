package com.grocery.jumarket.dto.view

import com.grocery.jumarket.dto.request.ItemVendidoDTO
import com.grocery.jumarket.dto.request.UsuarioDTO
import java.math.BigDecimal

data class VendaViewDTO(
    val id: Long,
    val usuario: UsuarioDTO,
    val valorTotal: BigDecimal,
    val formaDePagamento: String,
    val itensVendidos: List<ItemVendidoDTO>
)