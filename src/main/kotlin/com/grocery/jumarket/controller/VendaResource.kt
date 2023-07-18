package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.FinalizarVendaDTO
import com.grocery.jumarket.service.impl.VendaService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/venda")
class VendaResource(
    private val vendaService: VendaService
) {
    // finaliza uma venda
    @PostMapping
    fun finalizarVenda(@RequestBody finalizarVendaDTO: FinalizarVendaDTO) {
        vendaService.finalizarVenda(finalizarVendaDTO)
    }
}