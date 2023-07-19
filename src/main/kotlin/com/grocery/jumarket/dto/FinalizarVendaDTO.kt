package com.grocery.jumarket.dto

import com.grocery.jumarket.ennumeration.FormaDePagamento

data class FinalizarVendaDTO(
    val usuarioId: Long,
    val formaDePagamento: FormaDePagamento,
    val itensVendidos: List<ItemVendidoDTO>
)