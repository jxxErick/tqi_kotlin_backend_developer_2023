package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Produto
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.dto.NewProdutoDTO

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
    override fun criarProduto(produtoDTO: NewProdutoDTO): ProdutoDTO {
        // Verifica se a categoria existe
        val categoria = categoriaRepository.findById(produtoDTO.categoriaId)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        val novoProduto = Produto(
            nome = produtoDTO.nome,
            unidadeDeMedida = produtoDTO.unidadeDeMedida,
            precoUnitario = produtoDTO.precoUnitario,
            categoria = categoria,
            quantidadeEstoque = produtoDTO.quantidadeEstoque // Salvando a quantidade de estoque inicial
        )

        val produtoSalvo = produtoRepository.save(novoProduto)

        return ProdutoDTO(
            id = produtoSalvo.id,
            nome = produtoSalvo.nome,
            unidadeDeMedida = produtoSalvo.unidadeDeMedida,
            precoUnitario = produtoSalvo.precoUnitario,
            categoriaId = produtoSalvo.categoria.id,
            quantidadeEstoque = produtoSalvo.quantidadeEstoque
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
                    categoriaId = produto.categoria.id,
                quantidadeEstoque = produto.quantidadeEstoque
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
                categoriaId = produto.categoria.id,
            quantidadeEstoque = produto.quantidadeEstoque
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
                categoriaId = produtoAtualizado.categoria.id,
            quantidadeEstoque = produtoAtualizado.quantidadeEstoque
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
                categoriaId = produto.categoria.id,
                quantidadeEstoque = produto.quantidadeEstoque
            )
        }
    }

    override  fun deletarProduto(id: Long) {
        if (!produtoRepository.existsById(id)) {
            throw NotFoundException("Produto não encontrado")
        }

        produtoRepository.deleteById(id)
    }

   override fun atualizarEstoque(id: Long, quantidade: Long): ProdutoDTO {
       val produto = produtoRepository.findById(id)
           .orElseThrow { NotFoundException("Produto não encontrado") }

       val estoqueAtualizado = produto.quantidadeEstoque + quantidade
       produto.quantidadeEstoque = estoqueAtualizado
       val produtoAtualizado = produtoRepository.save(produto)

       return ProdutoDTO(
           id = produtoAtualizado.id,
           nome = produtoAtualizado.nome,
           unidadeDeMedida = produtoAtualizado.unidadeDeMedida,
           precoUnitario = produtoAtualizado.precoUnitario,
           categoriaId = produtoAtualizado.categoria.id,
           quantidadeEstoque = produtoAtualizado.quantidadeEstoque
       )
   }

    override fun removerItensDoEstoque(id: Long, quantidade: Long): ProdutoDTO {
        val produto = produtoRepository.findById(id)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        if (produto.quantidadeEstoque < quantidade) {
            throw EstoqueInsuficienteException("Não há estoque suficiente para remover a quantidade desejada.")
        }

        val estoqueAtualizado = produto.quantidadeEstoque - quantidade
        produto.quantidadeEstoque = estoqueAtualizado
        val produtoAtualizado = produtoRepository.save(produto)

        return ProdutoDTO(
            id = produtoAtualizado.id,
            nome = produtoAtualizado.nome,
            unidadeDeMedida = produtoAtualizado.unidadeDeMedida,
            precoUnitario = produtoAtualizado.precoUnitario,
            categoriaId = produtoAtualizado.categoria.id,
            quantidadeEstoque = produtoAtualizado.quantidadeEstoque
        )
    }
}