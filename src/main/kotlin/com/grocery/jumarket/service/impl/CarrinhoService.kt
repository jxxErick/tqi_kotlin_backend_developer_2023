package com.grocery.jumarket.service.impl


import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.domain.Produto
import com.grocery.jumarket.dto.CarrinhoDTO
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.ennumeration.StatusCarrinho
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CarrinhoService(
    private val carrinhoRepository: CarrinhoRepository,
    private val produtoRepository: ProdutoRepository
) {
    fun adicionarUmProdutoAoCarrinho(carrinhoDto: CarrinhoDTO) {
        val carrinho = carrinhoRepository.findById(carrinhoDto.carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }

        if (carrinho.status == StatusCarrinho.FINALIZADO) {
            throw BusinessException("Não é possível adicionar itens a um carrinho finalizado.")
        }

        val produto = produtoRepository.findById(carrinhoDto.produtoId)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        carrinho.adicionarProduto(produto)
        carrinhoRepository.save(carrinho)
    }

    fun removerItemDoCarrinhoPorId(carrinhoId: Long, produtoId: Long) {
        val carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }

        if (carrinho.status == StatusCarrinho.FINALIZADO) {
            throw BusinessException("Não é possível remover itens de um carrinho finalizado.")
        }

        carrinho.removerProdutoPorId(produtoId)
        carrinhoRepository.save(carrinho)
    }
}

