package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.FinalizarVendaDTO
import com.grocery.jumarket.dto.VendaDTO
import com.grocery.jumarket.service.impl.VendaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/venda")
class VendaResource(
    private val vendaService: VendaService
) {
    // finaliza uma venda
    @PostMapping("/finalizar")
    fun finalizarVenda(@RequestBody finalizarVendaDTO: FinalizarVendaDTO): ResponseEntity<VendaDTO> {
        val vendaDTO = vendaService.finalizarVenda(finalizarVendaDTO)
        return ResponseEntity(vendaDTO, HttpStatus.CREATED)
    }
    // lista as vendas
    @GetMapping
    fun listarVendas(): ResponseEntity<List<VendaDTO>> {
        val vendas = vendaService.listarVendas()
        return ResponseEntity.ok(vendas)
    }
}