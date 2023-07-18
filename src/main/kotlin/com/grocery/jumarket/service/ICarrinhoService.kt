package com.grocery.jumarket.service


import com.grocery.jumarket.dto.CarrinhoDTO
import com.grocery.jumarket.dto.NewCarrinhoDTO
import com.grocery.jumarket.dto.ProdutoDTO

interface ICarrinhoService {
    fun adicionarUmProdutoAoCarrinho(carrinhoDto: CarrinhoDTO)
    fun removerItemDoCarrinhoPorId(carrinhoId: Long, produtoId: Long)
    fun listarProdutosDoCarrinho(carrinhoId: Long): NewCarrinhoDTO
}
