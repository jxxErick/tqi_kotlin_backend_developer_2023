package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.dto.NewProdutoDTO
import com.grocery.jumarket.service.ProdutoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/produtos")
class ProdutoResource(private val produtoService: ProdutoService) {


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun criarProduto(@RequestBody produtoDTO: NewProdutoDTO): ResponseEntity<ProdutoDTO> {
        val novoProduto = produtoService.criarProduto(produtoDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto)
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarProdutos(): ResponseEntity<List<ProdutoDTO>> {
        val produtos = produtoService.listarProdutos()
        return ResponseEntity.ok(produtos)
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getProdutoPorId(@PathVariable id: Long): ResponseEntity<ProdutoDTO> {
        val produto = produtoService.getProdutoPorId(id)
        return ResponseEntity.ok(produto)
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun atualizarProduto(@PathVariable id: Long, @RequestBody produtoDTO: NewProdutoDTO): ResponseEntity<ProdutoDTO> {
        val produtoAtualizado = produtoService.atualizarProduto(id, produtoDTO)
        return ResponseEntity.ok(produtoAtualizado)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarProduto(@PathVariable id: Long) {
        produtoService.deletarProduto(id)
    }

    @GetMapping("/categoria/{categoriaId}")
    @ResponseStatus(HttpStatus.OK)
    fun listarProdutosPorCategoria(@PathVariable categoriaId: Long): ResponseEntity<List<ProdutoDTO>> {
        val produtos = produtoService.listarProdutosPorCategoria(categoriaId)
        return ResponseEntity.ok(produtos)
    }
    @PutMapping("/{id}/estoque/add")
    @ResponseStatus(HttpStatus.OK)
    fun atualizarEstoque(@PathVariable id: Long, @RequestBody quantidade: Long): ResponseEntity<ProdutoDTO> {
        val produtoAtualizado = produtoService.atualizarEstoque(id, quantidade)
        return ResponseEntity.ok(produtoAtualizado)
    }

    @PutMapping("/{id}/estoque/remover")
    @ResponseStatus(HttpStatus.OK)
    fun removerItensDoEstoque(@PathVariable id: Long, @RequestParam quantidade: Long): ResponseEntity<ProdutoDTO> {
        val produtoAtualizado = produtoService.removerItensDoEstoque(id, quantidade)
        return ResponseEntity.ok(produtoAtualizado)
    }
}

