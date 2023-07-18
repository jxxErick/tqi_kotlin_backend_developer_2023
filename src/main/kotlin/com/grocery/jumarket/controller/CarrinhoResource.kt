package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.CarrinhoDTO
import com.grocery.jumarket.service.impl.CarrinhoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/carrinho")
class CarrinhoResource(
    private val carrinhoService: CarrinhoService
) {
    // Adciona um produto ao carrinho
    @PostMapping
    fun adicionarProdutoAoCarrinho(@RequestBody carrinhoDto: CarrinhoDTO) {
        carrinhoService.adicionarUmProdutoAoCarrinho(carrinhoDto)
    }

    // Remove um produto do carrinho pela id do produto
    @DeleteMapping("/{carrinhoId}/produtos/{produtoId}")
    fun removerItemDoCarrinhoPorId(
        @PathVariable carrinhoId: Long,
        @PathVariable produtoId: Long
    ) {
        carrinhoService.removerItemDoCarrinhoPorId(carrinhoId, produtoId)
    }
}