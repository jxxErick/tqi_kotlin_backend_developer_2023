package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Produto

import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.service.exception.EstoqueInsuficienteException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ProdutoService(
        private val produtoRepository: ProdutoRepository,
        private val categoriaRepository: CategoriaRepository
) : IProdutoService {
    override fun criarProduto(produto: Produto): Produto {
        // Verifica se a categoria existe
        val categoria = categoriaRepository.findById(produto.categoria.id!!)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        produto.categoria = categoria
        return produtoRepository.save(produto)
    }

    override fun listarProdutos(): List<Produto> {
        return produtoRepository.findAll()
    }

    override fun getProdutoPorId(id: Long): Produto {
        return produtoRepository.findById(id)
            .orElseThrow { NotFoundException("Produto não encontrado") }
    }

    override fun atualizarProduto(produto: Produto): Produto {
        val produtoExistente = produtoRepository.findById(produto.id!!)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        produtoExistente.nome = produto.nome
        produtoExistente.unidadeDeMedida = produto.unidadeDeMedida
        produtoExistente.precoUnitario = produto.precoUnitario

        val categoria = categoriaRepository.findById(produto.categoria.id!!)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        produtoExistente.categoria = categoria

        return produtoRepository.save(produtoExistente)
    }

    override fun listarProdutosPorCategoria(categoriaId: Long): List<Produto> {
        val categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        return produtoRepository.findByCategoria(categoria)
    }

    override fun deletarProduto(id: Long) {
        if (!produtoRepository.existsById(id)) {
            throw NotFoundException("Produto não encontrado")
        }

        produtoRepository.deleteById(id)
    }

    override fun atualizarEstoque(id: Long, quantidade: Long): Produto {
        val produto = produtoRepository.findById(id)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        val estoqueAtualizado = produto.quantidadeEstoque + quantidade
        produto.quantidadeEstoque = estoqueAtualizado

        return produtoRepository.save(produto)
    }

    override fun removerItensDoEstoque(id: Long, quantidade: Long): Produto {
        val produto = produtoRepository.findById(id)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        if (produto.quantidadeEstoque < quantidade) {
            throw EstoqueInsuficienteException("Não há estoque suficiente para remover a quantidade desejada.")
        }

        val estoqueAtualizado = produto.quantidadeEstoque - quantidade
        produto.quantidadeEstoque = estoqueAtualizado

        return produtoRepository.save(produto)
    }
}