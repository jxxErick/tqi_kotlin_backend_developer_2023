package com.grocery.jumarket.service


import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.dto.CarrinhoDTO
import com.grocery.jumarket.dto.ItemCarrinhoDTO
import com.grocery.jumarket.dto.NewCarrinhoDTO
import com.grocery.jumarket.dto.ProdutoDTO

interface ICarrinhoService {
    fun adicionarItem(carrinhoDTO: CarrinhoDTO)
    fun removerItem(carrinhoId: Long, produtoId: Long)
    fun listarItens(carrinhoId: Long): List<ItemCarrinhoDTO>
    fun deletarCarrinho(usuarioId: Long)


}
