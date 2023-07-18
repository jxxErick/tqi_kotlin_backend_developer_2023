package com.grocery.jumarket.service

import com.grocery.jumarket.dto.FinalizarVendaDTO
import com.grocery.jumarket.dto.VendaDTO

interface IVendaService {
    fun finalizarVenda(vendaDto: FinalizarVendaDTO)
    fun listarProdutosVendidos(idVenda: Long): VendaDTO?
}