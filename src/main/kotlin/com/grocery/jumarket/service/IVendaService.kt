package com.grocery.jumarket.service

import com.grocery.jumarket.dto.FinalizarVendaDTO

interface IVendaService {
    fun finalizarVenda(vendaDto: FinalizarVendaDTO)
}