package com.grocery.jumarket.service


import com.grocery.jumarket.dto.CarrinhoDTO

interface ICarrinhoService {
    fun adicionarUmProdutoAoCarrinho(carrinhoDto: CarrinhoDTO)
    fun removerItemDoCarrinhoPorId(carrinhoId: Long, produtoId: Long)
}
