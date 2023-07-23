package com.grocery.jumarket.dto.request

data class FinalizarVendaDTO(
    val formaDePagamento: String,
    val usuarioId: Long
)