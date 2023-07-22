package com.grocery.jumarket.dto.request

import com.grocery.jumarket.ennumeration.FormaDePagamento

data class FinalizarVendaDTO(
    val usuarioId: Long,
    val formaDePagamento: FormaDePagamento
)