package com.grocery.jumarket.dto

import com.grocery.jumarket.ennumeration.FormaDePagamento

data class VendaDTO (
    val idVenda: Long,
    val valorTotal: Double,
    val formaDePagamento: FormaDePagamento,
    val produtos: List<NewProdutoDTO>

)
