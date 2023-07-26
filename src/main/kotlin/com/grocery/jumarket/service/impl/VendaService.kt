package com.grocery.jumarket.service.impl

import com.grocery.jumarket.domain.ItemVendido
import com.grocery.jumarket.domain.Venda
import com.grocery.jumarket.ennumeration.FormaDePagamento
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.repositories.VendaRepository
import com.grocery.jumarket.service.IVendaService
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.EstoqueInsuficienteException
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
    @Transactional
    class VendaService(
        private val vendaRepository: VendaRepository,
        private val usuarioRepository: UsuarioRepository,
        private val carrinhoRepository: CarrinhoRepository
    ) : IVendaService {

        override fun finalizarVenda(formaDePagamento: FormaDePagamento, usuarioId: Long): Venda {

            val usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow { NotFoundException("Usuário não encontrado") }

            val carrinhoDoUsuario = usuario.carrinho
                ?: throw NotFoundException("Usuario não possui carrinho disponivel, adicione itens para depois finalizar.")


            if (carrinhoDoUsuario.itens.isEmpty()) {
                throw BusinessException("Carrinho Vazio.")
            }

            val venda = Venda(
                usuario = usuario,
                valorTotal = BigDecimal.ZERO,
                formaDePagamento = formaDePagamento
            )


            for (itemCarrinho in carrinhoDoUsuario.itens) {
                val produto = itemCarrinho.produto

                if (produto.quantidadeEstoque < itemCarrinho.quantidade) {
                    throw EstoqueInsuficienteException("Não há estoque suficiente para concluir a venda.")
                }

                val itemVendido = ItemVendido(
                    produto = produto,
                    quantidade = itemCarrinho.quantidade,
                    precoUnitario = produto.precoUnitario,
                )

                venda.adicionarItemVendido(itemVendido)
                venda.valorTotal += itemVendido.precoUnitario.multiply(BigDecimal.valueOf(itemVendido.quantidade))
                venda.dataVenda = LocalDate.now()
                produto.quantidadeEstoque -= itemCarrinho.quantidade
            }

            val vendaSalva = vendaRepository.save(venda)


            carrinhoDoUsuario.itens.clear()
            carrinhoRepository.save(carrinhoDoUsuario)

            return vendaSalva
        }

        override fun listarVendas(): List<Venda> {
            return vendaRepository.findAll()
        }

   override fun listarVendasPorData(data: LocalDate): List<Venda> {
        val vendas = vendaRepository.findAllByDataVenda(data)

       if (vendas.isEmpty()) {
           throw NotFoundException("Nenhuma venda encontrada para a data informada")
       }

       return vendas
    }
    }