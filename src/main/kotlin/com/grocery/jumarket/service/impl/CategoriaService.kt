package com.grocery.jumarket.service.impl

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.request.CategoriaDTO
import com.grocery.jumarket.dto.request.NewCategoriaDTO
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.service.ICategoriaService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CategoriaService(private val categoriaRepository: CategoriaRepository) : ICategoriaService {

    override fun criarCategoria(categoria: Categoria): Categoria {
        return categoriaRepository.save(categoria)
    }

    override fun listarCategorias(): List<Categoria> {
        return categoriaRepository.findAll()
    }

    override fun deletarCategoria(categoriaId: Long) {
        val categoriaExistente = categoriaRepository.findById(categoriaId)
            .orElseThrow { NotFoundException("Categoria não encontrada") }
        categoriaRepository.delete(categoriaExistente)
    }

    override fun editarCategoria(categoriaId: Long, categoria: Categoria): Categoria {
        val categoriaExistente = categoriaRepository.findById(categoriaId)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        categoriaExistente.nome = categoria.nome

        return categoriaRepository.save(categoriaExistente)
    }

    override fun buscarCategoriaPorId(id: Long): Categoria {
        return categoriaRepository.findById(id)
            .orElseThrow { NotFoundException("Categoria não encontrada") }
    }
}