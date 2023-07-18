package com.grocery.jumarket.service.impl

import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.Venda
import com.grocery.jumarket.dto.FinalizarVendaDTO
import com.grocery.jumarket.dto.NewProdutoDTO
import com.grocery.jumarket.dto.VendaDTO
import com.grocery.jumarket.ennumeration.StatusCarrinho
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.repositories.VendaRepository
import com.grocery.jumarket.service.IVendaService
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class VendaService(
    private val vendaRepository: VendaRepository,
    private val carrinhoRepository: CarrinhoRepository,
) : IVendaService{
     override fun finalizarVenda(vendaDto: FinalizarVendaDTO) {
        val carrinho = carrinhoRepository.findById(vendaDto.carrinhoId)
            .orElseThrow { NoSuchElementException("Carrinho não encontrado") }

        if (carrinho.status == StatusCarrinho.FINALIZADO) {
            throw IllegalStateException("O carrinho já está finalizado")
        }

        // Finaliza a venda
        val venda = Venda(
            valorTotal = carrinho.precoTotal,
            formaDePagamento = vendaDto.formaDePagamento,
            carrinho = carrinho,
            usuario = carrinho.usuario
        )
        vendaRepository.save(venda)

        // Define o carrinho como finalizado
        carrinho.status = StatusCarrinho.FINALIZADO
        carrinhoRepository.save(carrinho)

        // Cria um novo carrinho relacionado ao usuário da venda
        val novoCarrinho = Carrinho(usuario = carrinho.usuario, venda = null)
        carrinhoRepository.save(novoCarrinho)
    }
   override fun listarProdutosVendidos(idVenda: Long): VendaDTO? {
        val venda = vendaRepository.findById(idVenda)
            .orElseThrow { NotFoundException("Venda não encontrada") }

        val produtosVendidos = venda.carrinho?.produtos ?: emptyList()

        val produtosDTO = produtosVendidos.map { produto ->
            NewProdutoDTO(
                nome = produto.nome,
                unidadeDeMedida = produto.unidadeDeMedida,
                precoUnitario = produto.precoUnitario,
                categoriaId = produto.categoria.id!!
            )
        }

        val vendaDetalhesDTO = venda.id?.let {
            VendaDTO(
                idVenda = it,
                valorTotal = venda.valorTotal,
                formaDePagamento = venda.formaDePagamento,
                produtos = produtosDTO
            )
        }

        return vendaDetalhesDTO
    }
}