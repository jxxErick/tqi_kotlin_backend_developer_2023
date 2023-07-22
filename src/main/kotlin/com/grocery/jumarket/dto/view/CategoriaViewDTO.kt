package com.grocery.jumarket.dto.view

import com.grocery.jumarket.domain.Categoria

data class CategoriaViewDTO(
    val id: Long,
    val nome: String
) {
    constructor(categoria: Categoria) : this(
        id = categoria.id ?: 0L,
        nome = categoria.nome
    )
}