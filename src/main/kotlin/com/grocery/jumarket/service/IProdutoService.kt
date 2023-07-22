package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Produto

interface IProdutoService {
    fun criarProduto(produto: Produto): Produto
    fun listarProdutos(): List<Produto>
    fun getProdutoPorId(id: Long): Produto
    fun atualizarProduto(produto: Produto): Produto
    fun deletarProduto(id: Long)
    fun listarProdutosPorCategoria(categoriaId: Long): List<Produto>
    fun atualizarEstoque(id: Long, quantidade: Long): Produto
    fun removerItensDoEstoque(id: Long, quantidade: Long): Produto
}