package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Venda
import com.grocery.jumarket.dto.request.FinalizarVendaDTO
import com.grocery.jumarket.dto.request.VendaDTO
import com.grocery.jumarket.ennumeration.FormaDePagamento

interface IVendaService {
    fun finalizarVenda(formaDePagamento: FormaDePagamento, usuarioId: Long): Venda
    fun listarVendas(): List<Venda>

}