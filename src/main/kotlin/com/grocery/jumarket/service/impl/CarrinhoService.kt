package com.grocery.jumarket.service.impl


import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.ItemCarrinho
import com.grocery.jumarket.domain.Usuario
import com.grocery.jumarket.dto.*
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.service.ICarrinhoService

import com.grocery.jumarket.service.exception.BusinessException
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

    //Adiciona item ao carrinho do usuario passando a id dele
    override fun adicionarItem(carrinhoDTO: CarrinhoDTO) {
        // Verifica se o usuario existe
        val usuario = usuarioRepository.findById(carrinhoDTO.usuarioId)
            .orElseThrow { NotFoundException("Usuário não encontrado") }
        // Caso o usuario n tenha nenhum carrinho, cria um
        val carrinho = usuario.carrinho ?: criarCarrinhoCasoAdicioneProduto(usuario)
        // verifica se o produto existe
        val produto = produtoRepository.findById(carrinhoDTO.produtoId)
            .orElseThrow { NotFoundException("Produto não encontrado") }
        // adiciona o produto ao carrinho
        carrinho.adicionarItem(ItemCarrinho(produto, carrinhoDTO.quantidade, produto.precoUnitario))
        carrinhoRepository.save(carrinho)
    }


   override fun removerItem(carrinhoId: Long, produtoId: Long) {
        val carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }

        carrinho.removerItem(produtoId)
        carrinhoRepository.save(carrinho)
    }

    //lista todos itens do carrinho
    override fun listarItens(carrinhoId: Long): List<ItemCarrinhoDTO> {
        val carrinho = carrinhoRepository.findById(carrinhoId)
            .orElseThrow { NotFoundException("Carrinho não encontrado") }

        return carrinho.itens.map { item ->
            ItemCarrinhoDTO(
                id = item.id!!,
                produto = ProdutoDTO(
                    id = item.produto.id!!,
                    nome = item.produto.nome,
                    unidadeDeMedida = item.produto.unidadeDeMedida,
                    precoUnitario = item.produto.precoUnitario,
                    categoriaId = null
                ),
                quantidade = item.quantidade,
                precoUnitario = item.precoUnitario
            )
        }
    }
    // essa função criei só pra auxiliar na de adcionar item, caso o usuario nao tenha um carrinho, ela cria automaticamente um
    fun criarCarrinhoCasoAdicioneProduto(usuario: Usuario): Carrinho {
        val carrinho = Carrinho(usuario = usuario)
        usuario.carrinho = carrinho
        return carrinhoRepository.save(carrinho)
    }
}