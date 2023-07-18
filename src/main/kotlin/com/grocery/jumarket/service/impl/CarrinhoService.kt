package com.grocery.jumarket.service.impl


import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.domain.Produto
import com.grocery.jumarket.dto.CarrinhoDTO
import com.grocery.jumarket.dto.NewCarrinhoDTO
import com.grocery.jumarket.dto.NewProdutoDTO
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.ennumeration.StatusCarrinho
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.service.ICarrinhoService

import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CarrinhoService (
    private val carrinhoRepository: CarrinhoRepository,
    private val produtoRepository: ProdutoRepository
): ICarrinhoService {
    override fun adicionarUmProdutoAoCarrinho(carrinhoDto: CarrinhoDTO) {
        // verifica se carrinho existe
        val carrinho = carrinhoRepository.findById(carrinhoDto.carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }
        // verifica se carrinho foi finalizado
        if (carrinho.status == StatusCarrinho.FINALIZADO) {
            throw BusinessException("Não é possível adicionar itens a um carrinho finalizado.")
        }
        //verifica se produto existe
        val produto = produtoRepository.findById(carrinhoDto.produtoId)
            .orElseThrow { NotFoundException("Produto não encontrado") }
        // salva
        carrinho.adicionarProduto(produto)
        carrinhoRepository.save(carrinho)
    }

    override fun removerItemDoCarrinhoPorId(carrinhoId: Long, produtoId: Long) {
        // verifica se carrinho existe
        val carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }
        // verifica se ja foi finalizado
        if (carrinho.status == StatusCarrinho.FINALIZADO) {
            throw BusinessException("Não é possível remover itens de um carrinho finalizado.")
        }
        // deleta
        carrinho.removerProdutoPorId(produtoId)
        carrinhoRepository.save(carrinho)
    }

    override fun listarProdutosDoCarrinho(carrinhoId: Long): NewCarrinhoDTO {
        val carrinho = obterCarrinhoPorId(carrinhoId)
        val produtos = carrinho.produtos.map { produto ->
            NewProdutoDTO(
                nome = produto.nome,
                unidadeDeMedida = produto.unidadeDeMedida,
                precoUnitario = produto.precoUnitario,
                categoriaId = produto.categoria.id!!
            )
        }
        val carrinhoDTO = NewCarrinhoDTO(
            carrinhoId = carrinho.id!!,
            produtos = produtos,
            precoTotal = carrinho.precoTotal
        )
        return carrinhoDTO
    }

    private fun obterCarrinhoPorId(carrinhoId: Long): Carrinho {
        return carrinhoRepository.findById(carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }
    }
}
