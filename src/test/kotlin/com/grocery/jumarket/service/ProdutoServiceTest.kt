package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.domain.Produto
import com.grocery.jumarket.dto.NewProdutoDTO
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.service.exception.EstoqueInsuficienteException
import com.grocery.jumarket.service.exception.NotFoundException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ProdutoServiceTest {
    @MockK    lateinit var produtoRepository: ProdutoRepository
    @MockK lateinit var categoriaRepository: CategoriaRepository
    @InjectMockKs lateinit var produtoService: ProdutoService

    @Test
    fun `deve criar um novo produto`() {
        val produtoFake: NewProdutoDTO = construirProduto()
        val categoriaFake = Categoria(id = 1L, nome = "Eletrônicos")

        every { categoriaRepository.findById(produtoFake.categoriaId) } returns Optional.of(categoriaFake)

        val produtoCriado = Produto(
            nome = produtoFake.nome,
            unidadeDeMedida = produtoFake.unidadeDeMedida,
            precoUnitario = produtoFake.precoUnitario,
            categoria = categoriaFake,
            quantidadeEstoque = produtoFake.quantidadeEstoque
        )

        every { produtoRepository.save(any()) } returns produtoCriado

        val atual: ProdutoDTO = produtoService.criarProduto(produtoFake)

        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual).isExactlyInstanceOf(ProdutoDTO::class.java)
        Assertions.assertThat(atual.id).isEqualTo(produtoCriado.id)
        Assertions.assertThat(atual.nome).isEqualTo(produtoCriado.nome)
        Assertions.assertThat(atual.unidadeDeMedida).isEqualTo(produtoCriado.unidadeDeMedida)
        Assertions.assertThat(atual.precoUnitario).isEqualTo(produtoCriado.precoUnitario)
        Assertions.assertThat(atual.categoriaId).isEqualTo(produtoCriado.categoria.id)
        Assertions.assertThat(atual.quantidadeEstoque).isEqualTo(produtoCriado.quantidadeEstoque)
    }
    @Test
    fun `deve listar todos os produtos`() {
        val produtosFake = listOf(
            Produto(id = 1L, nome = "Produto 1", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("10.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 100L),
            Produto(id = 2L, nome = "Produto 2", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("20.00"), categoria = Categoria(id = 2L, nome = "Roupas"), quantidadeEstoque = 50L),
            Produto(id = 3L, nome = "Produto 3", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("15.00"), categoria = Categoria(id = 3L, nome = "Alimentos"), quantidadeEstoque = 75L)
        )
        every { produtoRepository.findAll() } returns produtosFake

        val atual: List<ProdutoDTO> = produtoService.listarProdutos()

        Assertions.assertThat(atual).hasSize(produtosFake.size)
        for (i in produtosFake.indices) {
            Assertions.assertThat(atual[i].id).isEqualTo(produtosFake[i].id)
            Assertions.assertThat(atual[i].nome).isEqualTo(produtosFake[i].nome)
            Assertions.assertThat(atual[i].unidadeDeMedida).isEqualTo(produtosFake[i].unidadeDeMedida)
            Assertions.assertThat(atual[i].precoUnitario).isEqualTo(produtosFake[i].precoUnitario)
            Assertions.assertThat(atual[i].categoriaId).isEqualTo(produtosFake[i].categoria.id)
            Assertions.assertThat(atual[i].quantidadeEstoque).isEqualTo(produtosFake[i].quantidadeEstoque)
        }
    }

    @Test
    fun `deve buscar um produto por ID`() {
        val produtoFake = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 10L)
        every { produtoRepository.findById(1L) } returns Optional.of(produtoFake)

        val atual: ProdutoDTO = produtoService.getProdutoPorId(1L)

        Assertions.assertThat(atual).isNotNull
        Assertions.assertThat(atual.id).isEqualTo(produtoFake.id)
        Assertions.assertThat(atual.nome).isEqualTo(produtoFake.nome)
        Assertions.assertThat(atual.unidadeDeMedida).isEqualTo(produtoFake.unidadeDeMedida)
        Assertions.assertThat(atual.precoUnitario).isEqualTo(produtoFake.precoUnitario)
        Assertions.assertThat(atual.categoriaId).isEqualTo(produtoFake.categoria.id)
        Assertions.assertThat(atual.quantidadeEstoque).isEqualTo(produtoFake.quantidadeEstoque)
    }

    @Test
    fun `deve lançar NotFoundException ao buscar produto por ID inexistente`() {
        every { produtoRepository.findById(1L) } returns Optional.empty()

        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { produtoService.getProdutoPorId(1L) }
            .withMessage("Produto não encontrado")

        verify(exactly = 1) { produtoRepository.findById(1L) }
    }

    @Test
    fun `deve deletar um produto existente`() {
        val produtoExistente = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 10L)
        every { produtoRepository.existsById(produtoExistente.id!!) } returns true
        every { produtoRepository.deleteById(produtoExistente.id!!) } returns Unit

        produtoService.deletarProduto(produtoExistente.id!!)

        verify { produtoRepository.deleteById(produtoExistente.id!!) }
    }
    @Test
    fun `deve listar produtos por categoria`() {
        
        val categoriaId = 1L
        val categoria = Categoria(id = categoriaId, nome = "Eletrônicos")
        every { categoriaRepository.findById(categoriaId) } returns Optional.of(categoria)

        val produtos = listOf(
            Produto(id = 1L, nome = "Produto 1", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = categoria, quantidadeEstoque = 10L),
            Produto(id = 2L, nome = "Produto 2", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("150.00"), categoria = categoria, quantidadeEstoque = 15L),
            Produto(id = 3L, nome = "Produto 3", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("200.00"), categoria = categoria, quantidadeEstoque = 20L)
        )
        every { produtoRepository.findByCategoria(categoria) } returns produtos


        val listaDeProdutos: List<ProdutoDTO> = produtoService.listarProdutosPorCategoria(categoriaId)


        Assertions.assertThat(listaDeProdutos).isNotNull()
        Assertions.assertThat(listaDeProdutos).hasSize(3)


        listaDeProdutos.forEach {
            Assertions.assertThat(it.categoriaId).isEqualTo(categoriaId)
        }
    }

    @Test
    fun `deve lançar NotFoundException ao deletar produto inexistente`() {
        every { produtoRepository.existsById(1L) } returns false

        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { produtoService.deletarProduto(1L) }
            .withMessage("Produto não encontrado")

        verify(exactly = 1) { produtoRepository.existsById(1L) }
        verify(exactly = 0) { produtoRepository.deleteById(any()) }
    }

    @Test
    fun `deve atualizar um produto existente`() {
        val produtoExistente = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 10L)
        every { produtoRepository.findById(produtoExistente.id!!) } returns Optional.of(produtoExistente)

        val newProdutoDTO = NewProdutoDTO(nome = "Produto Atualizado", unidadeDeMedida = "Kilograma", precoUnitario = BigDecimal("50.00"), categoriaId = 2L, quantidadeEstoque = 20L)
        val produtoAtualizado = Produto(id = 1L, nome = "Produto Atualizado", unidadeDeMedida = "Kilograma", precoUnitario = BigDecimal("50.00"), categoria = Categoria(id = 2L, nome = "Roupas"), quantidadeEstoque = 20L)
        every { produtoRepository.save(any()) } returns produtoAtualizado

        val atual: ProdutoDTO = produtoService.atualizarProduto(produtoExistente.id!!, newProdutoDTO)

        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual.id).isEqualTo(produtoExistente.id)
        Assertions.assertThat(atual.nome).isEqualTo(newProdutoDTO.nome)
        Assertions.assertThat(atual.unidadeDeMedida).isEqualTo(newProdutoDTO.unidadeDeMedida)
        Assertions.assertThat(atual.precoUnitario).isEqualTo(newProdutoDTO.precoUnitario)
        Assertions.assertThat(atual.categoriaId).isEqualTo(newProdutoDTO.categoriaId)
        Assertions.assertThat(atual.quantidadeEstoque).isEqualTo(newProdutoDTO.quantidadeEstoque)
    }

    @Test
    fun `deve lançar NotFoundException ao atualizar produto inexistente`() {
        every { produtoRepository.findById(1L) } returns Optional.empty()

        val newProdutoDTO = NewProdutoDTO(nome = "Produto Atualizado", unidadeDeMedida = "Kilograma", precoUnitario = BigDecimal("50.00"), categoriaId = 2L, quantidadeEstoque = 20L)

        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { produtoService.atualizarProduto(1L, newProdutoDTO) }
            .withMessage("Produto não encontrado")

        verify(exactly = 1) { produtoRepository.findById(1L) }
        verify(exactly = 0) { produtoRepository.save(any()) }
    }

    @Test
    fun `deve atualizar o estoque de um produto existente`() {

        val produtoExistente = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 10L)
        every { produtoRepository.findById(produtoExistente.id!!) } returns Optional.of(produtoExistente)

        val quantidadeAtualizada = 5L
        val estoqueEsperado = quantidadeAtualizada + produtoExistente.quantidadeEstoque
        val produtoEstoqueAtualizado = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = estoqueEsperado)
        every { produtoRepository.save(any()) } returns produtoEstoqueAtualizado


        val atual: ProdutoDTO = produtoService.atualizarEstoque(produtoExistente.id!!, quantidadeAtualizada)


        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual.quantidadeEstoque).isEqualTo(estoqueEsperado)
    }
    @Test
    fun `deve lançar NotFoundException ao atualizar estoque de produto inexistente`() {
        every { produtoRepository.findById(1L) } returns Optional.empty()

        val quantidadeAtualizada = 5L

        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { produtoService.atualizarEstoque(1L, quantidadeAtualizada) }
            .withMessage("Produto não encontrado")

        verify(exactly = 1) { produtoRepository.findById(1L) }
        verify(exactly = 0) { produtoRepository.save(any()) }
    }

    @Test
    fun `deve remover itens do estoque de um produto existente`() {

        val produtoExistente = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 10L)
        every { produtoRepository.findById(produtoExistente.id!!) } returns Optional.of(produtoExistente)

        val quantidadeRemovida = 5L
        val estoqueEsperado = produtoExistente.quantidadeEstoque - quantidadeRemovida
        val produtoEstoqueAtualizado = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = estoqueEsperado)
        every { produtoRepository.save(any()) } returns produtoEstoqueAtualizado


        val atual: ProdutoDTO = produtoService.removerItensDoEstoque(produtoExistente.id!!, quantidadeRemovida)


        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual.quantidadeEstoque).isEqualTo(estoqueEsperado)
    }

    @Test
    fun `deve lançar EstoqueInsuficienteException ao remover itens do estoque com quantidade maior que o estoque atual`() {
        val produtoExistente = Produto(id = 1L, nome = "Produto Teste", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = Categoria(id = 1L, nome = "Eletrônicos"), quantidadeEstoque = 10L)
        every { produtoRepository.findById(produtoExistente.id!!) } returns Optional.of(produtoExistente)

        val quantidadeRemovida = 20L

        Assertions.assertThatExceptionOfType(EstoqueInsuficienteException::class.java)
            .isThrownBy { produtoService.removerItensDoEstoque(produtoExistente.id!!, quantidadeRemovida) }
            .withMessage("Não há estoque suficiente para remover a quantidade desejada.")

        verify(exactly = 1) { produtoRepository.findById(produtoExistente.id!!) }
        verify(exactly = 0) { produtoRepository.save(any()) }
    }



    private fun construirProduto(
        nome: String = "Produto Teste",
        unidadeDeMedida: String = "Unidade",
        precoUnitario: BigDecimal = BigDecimal("100.00"),
        categoriaId: Long = 1L,
        quantidadeEstoque: Long = 10L
    ) = NewProdutoDTO(
        nome = nome,
        unidadeDeMedida = unidadeDeMedida,
        precoUnitario = precoUnitario,
        categoriaId = categoriaId,
        quantidadeEstoque = quantidadeEstoque
    )

}
