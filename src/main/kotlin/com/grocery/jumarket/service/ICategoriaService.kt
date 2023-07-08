package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.CategoriaDTO
import com.grocery.jumarket.dto.NewCategoriaDTO
import jakarta.persistence.Id

interface ICategoriaService {
    fun criarCategoria(categoriaDTO: CategoriaDTO): CategoriaDTO
    fun listarCategorias(): List<CategoriaDTO>
    fun deletarCategoria(id: Long)
    fun editarCategoria(cateoriaId: Long, newCategoriaDTO: NewCategoriaDTO): Categoria
    fun getCategoriaPorId(id: Long): CategoriaDTO
}