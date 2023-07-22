package com.grocery.jumarket.dto.view

import com.grocery.jumarket.domain.Produto
import java.math.BigDecimal

data class ProdutoViewDTO(
    val id: Long,
    val nome: String,
    val unidadeDeMedida: String,
    val precoUnitario: BigDecimal,
    val categoriaId: Long,
    val quantidadeEstoque: Long
) {
    constructor(produto: Produto) : this(
        id = produto.id!!,
        nome = produto.nome,
        unidadeDeMedida = produto.unidadeDeMedida,
        precoUnitario = produto.precoUnitario,
        categoriaId = produto.categoria.id!!,
        quantidadeEstoque = produto.quantidadeEstoque
    )
}