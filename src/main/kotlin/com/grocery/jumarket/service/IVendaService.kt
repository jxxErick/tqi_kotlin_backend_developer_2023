package com.grocery.jumarket.service

import com.grocery.jumarket.dto.request.FinalizarVendaDTO
import com.grocery.jumarket.dto.request.VendaDTO

interface IVendaService {
    fun finalizarVenda(finalizarVendaDTO: FinalizarVendaDTO): VendaDTO
    fun listarVendas(): List<VendaDTO>

}