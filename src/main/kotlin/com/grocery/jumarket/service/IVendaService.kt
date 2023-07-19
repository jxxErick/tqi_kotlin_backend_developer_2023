package com.grocery.jumarket.service

import com.grocery.jumarket.dto.FinalizarVendaDTO
import com.grocery.jumarket.dto.VendaDTO

interface IVendaService {
    fun finalizarVenda(finalizarVendaDTO: FinalizarVendaDTO): VendaDTO
    fun listarVendas(): List<VendaDTO>

}