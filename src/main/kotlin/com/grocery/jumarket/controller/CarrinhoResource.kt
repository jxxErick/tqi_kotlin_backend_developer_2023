package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.CarrinhoDTO
import com.grocery.jumarket.dto.ItemCarrinhoDTO
import com.grocery.jumarket.dto.NewCarrinhoDTO
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.service.impl.CarrinhoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/carrinho")
class CarrinhoResource(
    private val carrinhoService: CarrinhoService
) {
    // add itens passando a id do usuario
    @PostMapping("/add/{usuarioId}")
    fun adicionarItem(
        @PathVariable usuarioId: Long,
        @RequestBody carrinhoDTO: CarrinhoDTO
    ): ResponseEntity<Unit> {
        carrinhoDTO.usuarioId = usuarioId
        carrinhoService.adicionarItem(carrinhoDTO)
        return ResponseEntity(HttpStatus.CREATED)
    }

    // remove itens especificos do carrinho pela id
    @DeleteMapping("/{carrinhoId}/produtos/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removerItemDoCarrinho(
        @PathVariable carrinhoId: Long,
        @PathVariable produtoId: Long
    ) {
        carrinhoService.removerItem(carrinhoId, produtoId)
    }

    // lista o carrinho
    @GetMapping("/{carrinhoId}")
    @ResponseStatus(HttpStatus.OK)
    fun listarItensDoCarrinho(@PathVariable carrinhoId: Long): List<ItemCarrinhoDTO> {
        return carrinhoService.listarItens(carrinhoId)
    }

}
