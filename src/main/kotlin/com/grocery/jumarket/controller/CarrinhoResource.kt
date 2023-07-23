package com.grocery.jumarket.controller

import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.dto.request.ItemCarrinhoDTO
import com.grocery.jumarket.dto.request.ProdutoDTO
import com.grocery.jumarket.dto.view.ItemCarrinhoViewDTO
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
    fun adicionarItem(@PathVariable usuarioId: Long, @RequestBody request: ItemCarrinhoDTO): ResponseEntity<Unit> {
        carrinhoService.adicionarItemAoCarrinho(usuarioId, request.produtoId, request.quantidade)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @DeleteMapping("/{carrinhoId}/produtos/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removerItemDoCarrinho(@PathVariable carrinhoId: Carrinho, @PathVariable produtoId: Long) {
        carrinhoService.removerItem(carrinhoId, produtoId)
    }

    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    fun listarItensDoCarrinhoPorUsuario(@PathVariable usuarioId: Long): List<ItemCarrinhoViewDTO> {
        val itensCarrinho = carrinhoService.listarItensPorUsuario(usuarioId)
        return itensCarrinho.map { item ->
            ItemCarrinhoViewDTO(
                id = item.id ?: 0,
                produto = ProdutoDTO(
                    nome = item.produto.nome,
                    unidadeDeMedida = item.produto.unidadeDeMedida,
                    precoUnitario = item.produto.precoUnitario,
                    categoriaId = 0,
                    quantidadeEstoque = item.produto.quantidadeEstoque
                ),
                quantidade = item.quantidade,
                precoUnitario = item.precoUnitario
            )
        }
    }
    @DeleteMapping("/{usuarioId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarCarrinho(@PathVariable usuarioId: Long) = this.carrinhoService.deletarCarrinho(usuarioId)

    @GetMapping("/usuario/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    fun getCarrinhoPorUsuario(@PathVariable usuarioId: Long): Carrinho? {
        return carrinhoService.getCarrinhoPorUsuario(usuarioId)
    }

}
