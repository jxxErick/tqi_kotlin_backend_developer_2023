package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.request.FinalizarVendaDTO
import com.grocery.jumarket.dto.request.VendaDTO
import com.grocery.jumarket.service.impl.VendaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/venda")
class VendaResource(
    private val vendaService: VendaService
) {

    @PostMapping("/finalizar")
    fun finalizarVenda(@RequestBody finalizarVendaDTO: FinalizarVendaDTO): ResponseEntity<VendaDTO> {
        val vendaFinalizada = vendaService.finalizarVenda(finalizarVendaDTO)
        return ResponseEntity(vendaFinalizada, HttpStatus.CREATED)
    }

    @GetMapping
    fun listarVendas(): ResponseEntity<List<VendaDTO>> {
        val vendas = vendaService.listarVendas()
        return ResponseEntity.ok(vendas)
    }
}