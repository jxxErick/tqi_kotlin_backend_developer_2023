package com.grocery.jumarket.service.impl

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.CategoriaDTO
import com.grocery.jumarket.dto.NewCategoriaDTO
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.service.ICategoriaService
import org.springframework.stereotype.Service

@Service
class CategoriaService(private val categoriaRepository: CategoriaRepository) : ICategoriaService {
    override fun criarCategoria(categoriaDTO: CategoriaDTO): CategoriaDTO {
        val categoria = Categoria(nome = categoriaDTO.nome)
        val novaCategoria = categoriaRepository.save(categoria)
        return CategoriaDTO(novaCategoria.id, novaCategoria.nome)
    }

    override fun listarCategorias(): List<CategoriaDTO> {
        val categorias = categoriaRepository.findAll()
        return categorias.map { CategoriaDTO(it.id, it.nome) }
    }

    override fun deletarCategoria(categoriaId: Long) {
        val categoriaExistente = categoriaRepository.findById(categoriaId)
                .orElseThrow { NotFoundException("Categoria não encontrada") }

        categoriaRepository.deleteById(categoriaExistente.id!!)
    }

    override fun editarCategoria(categoriaId: Long, newCategoriaDTO: NewCategoriaDTO): Categoria {
        val categoriaExistente = categoriaRepository.findById(categoriaId)
                .orElseThrow { NotFoundException("Categoria não encontrada") }

        categoriaExistente.nome = newCategoriaDTO.nome
        val categoriaAtualizada = categoriaRepository.save(categoriaExistente)
        return Categoria(categoriaAtualizada.id, categoriaAtualizada.nome)
    }

    override fun getCategoriaPorId(id: Long): CategoriaDTO {
        val categoria = categoriaRepository.findById(id)
                .orElseThrow { NotFoundException("Categoria não encontrada") }

        return CategoriaDTO(categoria.id, categoria.nome)
    }
}
