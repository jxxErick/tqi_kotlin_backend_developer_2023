package com.grocery.jumarket.service

import com.grocery.jumarket.dto.NewProdutoDTO
import com.grocery.jumarket.dto.ProdutoDTO

interface IProdutoService {
    fun criarProduto(produtoDTO: NewProdutoDTO): ProdutoDTO
    fun listarProdutos(): List<ProdutoDTO>
    fun getProdutoPorId(id: Long): ProdutoDTO
    fun atualizarProduto(id: Long, produtoDTO: NewProdutoDTO): ProdutoDTO
    fun deletarProduto(id: Long)
    fun listarProdutosPorCategoria(categoriaId: Long): List<ProdutoDTO>
}