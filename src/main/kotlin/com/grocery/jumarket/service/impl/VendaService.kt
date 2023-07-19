package com.grocery.jumarket.service.impl

import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.ItemVendido
import com.grocery.jumarket.domain.Venda
import com.grocery.jumarket.dto.*
import com.grocery.jumarket.ennumeration.FormaDePagamento
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.repositories.VendaRepository
import com.grocery.jumarket.service.IVendaService
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

    @Service
    @Transactional
    class VendaService(
        private val vendaRepository: VendaRepository,
        private val usuarioRepository: UsuarioRepository,
        private val produtoRepository: ProdutoRepository,
        private val carrinhoRepository: CarrinhoRepository
    ) : IVendaService {
        //finaliza venda passando forma de pagamento, usuarioId e os itens
        override fun finalizarVenda(finalizarVendaDTO: FinalizarVendaDTO): VendaDTO {
            //verifica se o usuario existe
            val usuario = usuarioRepository.findById(finalizarVendaDTO.usuarioId)
                .orElseThrow { NotFoundException("Usuário não encontrado") }

            val venda = Venda(
                usuario = usuario,
                valorTotal = BigDecimal.ZERO,
                formaDePagamento = finalizarVendaDTO.formaDePagamento
            )
            //verifica se os itens existe e adiciona nos itens vendidos
            for (itemDTO in finalizarVendaDTO.itensVendidos) {
                val produto = produtoRepository.findById(itemDTO.produtoId)
                    .orElseThrow { NotFoundException("Produto não encontrado") }

                val itemVendido = ItemVendido(
                    produto = produto,
                    quantidade = itemDTO.quantidade,
                    precoUnitario = produto.precoUnitario
                )

                venda.adicionarItemVendido(itemVendido)
                venda.valorTotal += itemVendido.precoUnitario.multiply(BigDecimal.valueOf(itemVendido.quantidade))
            }

            val vendaSalva = vendaRepository.save(venda)

            // deleta o carrinho do usuario assim que for vendido
            val carrinhoDelete = usuario.carrinho
            if (carrinhoDelete != null) {
                carrinhoRepository.delete(carrinhoDelete)
            }

            return vendaSalva.toDTO()
        }

        private fun Venda.toDTO(): VendaDTO {
            val usuarioDTO = UsuarioDTO(
                id = usuario.id,
                email = usuario.email,
                nome = usuario.nome,
                cpf = usuario.cpf
            )

            val itensVendidosDTO = itensVendidos.map { itemVendido ->
                ItemVendidoDTO(
                    produtoId = itemVendido.produto.id!!,
                    quantidade = itemVendido.quantidade,
                    precoUnitario = itemVendido.precoUnitario
                )
            }

            return VendaDTO(
                id = id!!,
                usuario = usuarioDTO,
                valorTotal = valorTotal,
                formaDePagamento = formaDePagamento.name,
                itensVendidos = itensVendidosDTO
            )
        }
        override fun listarVendas(): List<VendaDTO> {
            val vendas = vendaRepository.findAll()
            return vendas.map { venda -> venda.toDTO() }
        }
    }