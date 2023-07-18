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

    //cria produto
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun criarProduto(@RequestBody produtoDTO: NewProdutoDTO): ResponseEntity<ProdutoDTO> {
        val novoProduto = produtoService.criarProduto(produtoDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto)
    }

    // lista produtos
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarProdutos(): ResponseEntity<List<ProdutoDTO>> {
        val produtos = produtoService.listarProdutos()
        return ResponseEntity.ok(produtos)
    }

    //pega produto por id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getProdutoPorId(@PathVariable id: Long): ResponseEntity<ProdutoDTO> {
        val produto = produtoService.getProdutoPorId(id)
        return ResponseEntity.ok(produto)
    }

    //atualiza produto por id
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun atualizarProduto(
            @PathVariable id: Long,
            @RequestBody produtoDTO: NewProdutoDTO
    ): ResponseEntity<ProdutoDTO> {
        val produtoAtualizado = produtoService.atualizarProduto(id, produtoDTO)
        return ResponseEntity.ok(produtoAtualizado)
    }
    // deleta produto por id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarProduto(@PathVariable id: Long) {
        produtoService.deletarProduto(id)
    }
    //Lista produtos pela categoria
    @GetMapping("/categoria/{categoriaId}")
    @ResponseStatus(HttpStatus.OK)
    fun listarProdutosPorCategoria(@PathVariable categoriaId: Long): ResponseEntity<List<ProdutoDTO>> {
        val produtos = produtoService.listarProdutosPorCategoria(categoriaId)
        return ResponseEntity.ok(produtos)
    }

}

