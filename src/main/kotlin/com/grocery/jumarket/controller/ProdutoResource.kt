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
    fun criarProduto(@RequestBody produtoDTO: NewProdutoDTO): ResponseEntity<ProdutoDTO> {
        val novoProduto = produtoService.criarProduto(produtoDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto)
    }

    @GetMapping
    fun listarProdutos(): ResponseEntity<List<ProdutoDTO>> {
        val produtos = produtoService.listarProdutos()
        return ResponseEntity.ok(produtos)
    }

    @GetMapping("/{id}")
    fun getProdutoPorId(@PathVariable id: Long): ResponseEntity<ProdutoDTO> {
        val produto = produtoService.getProdutoPorId(id)
        return ResponseEntity.ok(produto)
    }

    @PutMapping("/{id}")
    fun atualizarProduto(
            @PathVariable id: Long,
            @RequestBody produtoDTO: NewProdutoDTO
    ): ResponseEntity<ProdutoDTO> {
        val produtoAtualizado = produtoService.atualizarProduto(id, produtoDTO)
        return ResponseEntity.ok(produtoAtualizado)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarProduto(@PathVariable id: Long) {
        produtoService.deletarProduto(id)
    }
}

