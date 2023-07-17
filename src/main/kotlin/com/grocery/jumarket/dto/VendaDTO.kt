package com.grocery.jumarket.dto

import com.grocery.jumarket.ennumeration.formaDePagamento

data class VendaDTO(
    val id: Long?,
    val valorTotal: Double,
    val formaDePagamento: formaDePagamento,
    val usuario: UsuarioDTO?
)