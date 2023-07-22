package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.request.ProdutoDTO
import com.grocery.jumarket.dto.view.ProdutoViewDTO
import com.grocery.jumarket.service.ProdutoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/produtos")
class ProdutoResource(private val produtoService: ProdutoService) {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun salvarProduto(@RequestBody produtoDTO: ProdutoDTO): ResponseEntity<ProdutoViewDTO> {
        val produto = produtoService.criarProduto(produtoDTO.toProduto())
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoViewDTO(produto))
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarProdutos(): ResponseEntity<List<ProdutoViewDTO>> {
        val produtos = produtoService.listarProdutos()
        val produtosDTO = produtos.map { ProdutoViewDTO(it) }
        return ResponseEntity.ok(produtosDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun buscarProdutoPorId(@PathVariable id: Long): ResponseEntity<ProdutoViewDTO> {
        val produto = produtoService.getProdutoPorId(id)
        return ResponseEntity.ok(ProdutoViewDTO(produto))
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun atualizarProduto(@PathVariable id: Long, @RequestBody produtoDTO: ProdutoDTO): ResponseEntity<ProdutoViewDTO> {
        val produtoExistente = produtoService.getProdutoPorId(id)
        val produtoAtualizado = produtoDTO.toProduto().copy(id = id)
        val produtoAtualizadoResultado = produtoService.atualizarProduto(produtoAtualizado)
        return ResponseEntity.ok(ProdutoViewDTO(produtoAtualizadoResultado))
    }
    @GetMapping("/categoria/{categoriaId}")
    @ResponseStatus(HttpStatus.OK)
    fun listarProdutosPorCategoria(@PathVariable categoriaId: Long): ResponseEntity<List<ProdutoViewDTO>> {
        val produtos = produtoService.listarProdutosPorCategoria(categoriaId)
        val produtosDTO = produtos.map { ProdutoViewDTO(it) }
        return ResponseEntity.ok(produtosDTO)
    }

    @PostMapping("/{id}/add-estoque")
    @ResponseStatus(HttpStatus.OK)
    fun adicionarEstoque(@PathVariable id: Long, @RequestBody quantidade: Long): ResponseEntity<ProdutoViewDTO> {
        val produtoAtualizado = produtoService.atualizarEstoque(id, quantidade)
        return ResponseEntity.ok(ProdutoViewDTO(produtoAtualizado))
    }

    @PostMapping("/{id}/remover-stoque")
    @ResponseStatus(HttpStatus.OK)
    fun removerEstoque(@PathVariable id: Long, @RequestBody quantidade: Long): ResponseEntity<ProdutoViewDTO> {
        val produtoAtualizado = produtoService.removerItensDoEstoque(id, quantidade)
        return ResponseEntity.ok(ProdutoViewDTO(produtoAtualizado))
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarProduto(@PathVariable id: Long) {
        produtoService.deletarProduto(id)
    }
}
