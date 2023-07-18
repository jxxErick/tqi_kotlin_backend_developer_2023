package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Produto
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.dto.NewProdutoDTO

import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class ProdutoService(
        private val produtoRepository: ProdutoRepository,
        private val categoriaRepository: CategoriaRepository
) : IProdutoService {
    override fun criarProduto(produtoDTO: NewProdutoDTO): ProdutoDTO {
        // verifica se existe
        val categoria = categoriaRepository.findById(produtoDTO.categoriaId).orElseThrow { NotFoundException("Categoria não encontrada") }

        val novoProduto = Produto (
                nome = produtoDTO.nome,
                unidadeDeMedida = produtoDTO.unidadeDeMedida,
                precoUnitario = produtoDTO.precoUnitario,
                categoria = categoria
        )

        val produtoSalvo = produtoRepository.save(novoProduto)

        return ProdutoDTO(
                id = produtoSalvo.id,
                nome = produtoSalvo.nome,
                unidadeDeMedida = produtoSalvo.unidadeDeMedida,
                precoUnitario = produtoSalvo.precoUnitario,
                categoriaId = produtoSalvo.categoria.id
        )
    }

    override fun listarProdutos(): List<ProdutoDTO> {
        val produtos = produtoRepository.findAll()
        return produtos.map { produto ->
            ProdutoDTO(
                    id = produto.id,
                    nome = produto.nome,
                    unidadeDeMedida = produto.unidadeDeMedida,
                    precoUnitario = produto.precoUnitario,
                    categoriaId = produto.categoria.id
            )
        }
    }

    override  fun getProdutoPorId(id: Long): ProdutoDTO {
        val produto = produtoRepository.findById(id)
                .orElseThrow { NotFoundException("Produto não encontrado") }

        return ProdutoDTO(
                id = produto.id,
                nome = produto.nome,
                unidadeDeMedida = produto.unidadeDeMedida,
                precoUnitario = produto.precoUnitario,
                categoriaId = produto.categoria.id
        )
    }

    override fun atualizarProduto(id: Long, produtoDTO: NewProdutoDTO): ProdutoDTO {
        val produtoExistente = produtoRepository.findById(id)
                .orElseThrow { NotFoundException("Produto não encontrado") }

        produtoExistente.nome = produtoDTO.nome
        produtoExistente.unidadeDeMedida = produtoDTO.unidadeDeMedida
        produtoExistente.precoUnitario = produtoDTO.precoUnitario

        val produtoAtualizado = produtoRepository.save(produtoExistente)

        return ProdutoDTO(
                id = produtoAtualizado.id,
                nome = produtoAtualizado.nome,
                unidadeDeMedida = produtoAtualizado.unidadeDeMedida,
                precoUnitario = produtoAtualizado.precoUnitario,
                categoriaId = produtoAtualizado.categoria.id
        )
    }
    override fun listarProdutosPorCategoria(categoriaId: Long): List<ProdutoDTO> {
        val categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        val produtos = produtoRepository.findByCategoria(categoria)

        return produtos.map { produto ->
            ProdutoDTO(
                id = produto.id,
                nome = produto.nome,
                unidadeDeMedida = produto.unidadeDeMedida,
                precoUnitario = produto.precoUnitario,
                categoriaId = produto.categoria.id
            )
        }
    }

    override  fun deletarProduto(id: Long) {
        if (!produtoRepository.existsById(id)) {
            throw NotFoundException("Produto não encontrado")
        }

        produtoRepository.deleteById(id)
    }
}