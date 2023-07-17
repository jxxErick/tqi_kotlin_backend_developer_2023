package com.grocery.jumarket.dto

import com.grocery.jumarket.ennumeration.FormaDePagamento

data class FinalizarVendaDTO(
    val carrinhoId: Long,
    val formaDePagamento: FormaDePagamento
)