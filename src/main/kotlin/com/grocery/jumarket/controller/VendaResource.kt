package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.FinalizarVendaDTO
import com.grocery.jumarket.dto.VendaDTO
import com.grocery.jumarket.service.impl.VendaService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/venda")
class VendaResource(
    private val vendaService: VendaService
) {
    // finaliza uma venda
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun finalizarVenda(@RequestBody finalizarVendaDTO: FinalizarVendaDTO) {
        vendaService.finalizarVenda(finalizarVendaDTO)
    }
    @GetMapping("/{idVenda}")
    fun listarProdutosVendidosPorId(@PathVariable idVenda: Long): VendaDTO? {
        return vendaService.listarProdutosVendidos(idVenda)
    }
}