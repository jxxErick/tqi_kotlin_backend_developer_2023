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
        // Verifica se o usuário existe
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        // Verifica se o produto existe
        val produto = produtoRepository.findById(produtoId)
            .orElseThrow { NotFoundException("Produto não encontrado") }

        // Verifica se o produto possui estoque suficiente
        if (produto.quantidadeEstoque < quantidade) {
            throw EstoqueInsuficienteException("Não há estoque suficiente para adicionar o produto ao carrinho.")
        }

        // Buscar o carrinho do usuário, caso ele não tenha, cria um
        val carrinho = usuario.carrinho ?: criarCarrinhoCasoAdicioneProduto(usuario)

        // Adiciona o produto ao carrinho
        carrinho.adicionarItem(ItemCarrinho(produto, quantidade, produto.precoUnitario))
        carrinhoRepository.save(carrinho)
    }

    override fun removerItem(carrinho: Carrinho, produtoId: Long) {
        // Remove o item do carrinho
        carrinho.removerItem(produtoId)
        carrinhoRepository.save(carrinho)

        // Caso o carrinho fique vazio, apaga ele
        if (carrinho.itens.isEmpty()) {
            carrinhoRepository.delete(carrinho)
        }
    }

    override fun listarItensPorUsuario(usuarioId: Long): List<ItemCarrinho> {
        // Verifica se o usuário existe
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        // Verifica se o usuário possui carrinho
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
        return usuario.carrinho
    }

    fun criarCarrinhoCasoAdicioneProduto(usuario: Usuario): Carrinho {
        val carrinho = Carrinho(usuario = usuario)
        usuario.carrinho = carrinho
        return carrinhoRepository.save(carrinho)
    }
}