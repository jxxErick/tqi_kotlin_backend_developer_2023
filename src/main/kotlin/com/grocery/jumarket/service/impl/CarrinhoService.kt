package com.grocery.jumarket.service.impl


import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.ItemCarrinho
import com.grocery.jumarket.domain.Usuario

import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.service.ICarrinhoService

import com.grocery.jumarket.service.exception.EstoqueInsuficienteException
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CarrinhoService (
    private val carrinhoRepository: CarrinhoRepository,
    private val produtoRepository: ProdutoRepository,
    private val usuarioRepository: UsuarioRepository
): ICarrinhoService {

    override fun adicionarItemAoCarrinho(usuarioId: Long, produtoId: Long, quantidade: Long) {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        val produto = produtoRepository.findById(produtoId)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        if (produto.quantidadeEstoque < quantidade) {
            throw EstoqueInsuficienteException("Não há estoque suficiente para adicionar o produto ao carrinho.")
        }

        val carrinho = usuario.carrinho ?: criarCarrinhoCasoAdicioneProduto(usuario)

        val itemExistente = carrinho.itens.find { it.produto == produto }

        if (itemExistente != null) {
            itemExistente.quantidade += quantidade
            itemExistente.precoUnitario = produto.precoUnitario
        } else {
            val novoItem = ItemCarrinho(produto, quantidade, produto.precoUnitario)
            carrinho.adicionarItem(novoItem)
        }
        carrinhoRepository.save(carrinho)
    }

    override fun removerItemDoCarrinho(carrinho: Carrinho, produtoId: Long) {
        val carrinhoExistente = carrinhoRepository.findById(carrinho.id!!)
        if (carrinhoExistente.isEmpty) {
            throw NotFoundException("Carrinho não encontrado.")
        }

        if (carrinho.itens.none { it.id == produtoId }) {
            throw NotFoundException("Produto não encontrado no carrinho.")
        }

        carrinho.removerItem(produtoId)
        carrinhoRepository.save(carrinho)

        if (carrinho.itens.isEmpty()) {
            carrinhoRepository.delete(carrinho)
        }
    }

    override fun listarItensPorUsuario(usuarioId: Long): List<ItemCarrinho> {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        val carrinho = usuario.carrinho
            ?: throw NotFoundException("Carrinho não encontrado para o usuário")

        return carrinho.itens
    }

    override fun deletarCarrinho(usuarioId: Long) {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        val carrinho = usuario.carrinho
            ?: throw NotFoundException("Carrinho não encontrado para o usuário")

        carrinhoRepository.delete(carrinho)
    }
    override fun getCarrinhoPorUsuario(usuarioId: Long): Carrinho? {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }
        if(usuario.carrinho == null){
            throw NotFoundException("Carrinho do usuario vazio")
        } else {
        return usuario.carrinho
        }
    }

    fun criarCarrinhoCasoAdicioneProduto(usuario: Usuario): Carrinho {
        val carrinho = Carrinho(usuario = usuario)
        usuario.carrinho = carrinho
        return carrinhoRepository.save(carrinho)
    }
}