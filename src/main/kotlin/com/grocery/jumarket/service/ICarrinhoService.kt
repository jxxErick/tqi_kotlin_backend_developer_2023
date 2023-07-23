package com.grocery.jumarket.service


import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.ItemCarrinho

interface ICarrinhoService {
    fun adicionarItemAoCarrinho(usuarioId: Long, produtoId: Long, quantidade: Long)
    fun removerItemDoCarrinho(carrinho: Carrinho, produtoId: Long)
    fun listarItensPorUsuario(usuarioId: Long): List<ItemCarrinho>
    fun deletarCarrinho(usuarioId: Long)
    fun getCarrinhoPorUsuario(usuarioId: Long): Carrinho?


}
