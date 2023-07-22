package com.grocery.jumarket.service


import com.grocery.jumarket.dto.request.CarrinhoDTO
import com.grocery.jumarket.dto.request.ItemCarrinhoDTO

interface ICarrinhoService {
    fun adicionarItem(carrinhoDTO: CarrinhoDTO)
    fun removerItem(carrinhoId: Long, produtoId: Long)
    fun listarItens(carrinhoId: Long): List<ItemCarrinhoDTO>
    fun deletarCarrinho(usuarioId: Long)


}
