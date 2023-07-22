package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.request.CategoriaDTO
import com.grocery.jumarket.dto.request.NewCategoriaDTO


interface ICategoriaService {
    fun criarCategoria(categoria: Categoria): Categoria
    fun listarCategorias(): List<Categoria>
    fun deletarCategoria(categoriaId: Long)
    fun editarCategoria(categoriaId: Long, categoria: Categoria): Categoria
    fun buscarCategoriaPorId(id: Long): Categoria
}