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

    @PostMapping("/add/{usuarioId}")
    fun adicionarItem(@PathVariable usuarioId: Long, @RequestBody carrinhoDTO: CarrinhoDTO): ResponseEntity<Unit> {
        carrinhoDTO.usuarioId = usuarioId
        carrinhoService.adicionarItem(carrinhoDTO)
        return ResponseEntity(HttpStatus.CREATED)
    }


    @DeleteMapping("/{carrinhoId}/produtos/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removerItemDoCarrinho(@PathVariable carrinhoId: Long,@PathVariable produtoId: Long) {
        carrinhoService.removerItem(carrinhoId, produtoId)
    }


    @GetMapping("/{carrinhoId}")
    @ResponseStatus(HttpStatus.OK)
    fun listarItensDoCarrinho(@PathVariable carrinhoId: Long): List<ItemCarrinhoDTO> {
        return carrinhoService.listarItens(carrinhoId)
    }

    @DeleteMapping("/{usuarioId}")
    fun deletarCarrinho(@PathVariable usuarioId: Long): ResponseEntity<Void> {
        carrinhoService.deletarCarrinho(usuarioId)
        return ResponseEntity.noContent().build()
    }
}
