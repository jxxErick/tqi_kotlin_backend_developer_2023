package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.domain.Produto
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ProdutoServiceTest {
    @MockK
    lateinit var produtoRepository: ProdutoRepository
    @MockK
    lateinit var categoriaRepository: CategoriaRepository
    @InjectMockKs
    lateinit var produtoService: ProdutoService

    private lateinit var produtoFake: Produto
    private lateinit var categoriaFake: Categoria


    @BeforeEach
    fun setUp() {

        categoriaFake = Categoria(id = 1L, nome = "Eletrônicos")
        produtoFake = Produto(
            id = 1L,
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.00"),
            categoria = categoriaFake,
            quantidadeEstoque = 10L
        )
    }


    @Test
    fun `deve criar um novo produto`() {

        every { categoriaRepository.findById(produtoFake.categoria.id!!) } returns Optional.of(categoriaFake)
        every { produtoRepository.save(any()) } returns produtoFake


        val produtoCriado = produtoService.criarProduto(produtoFake)


        Assertions.assertThat(produtoCriado).isNotNull()
        Assertions.assertThat(produtoCriado).isEqualTo(produtoFake)
    }


    @Test
    fun `deve listar todos os produtos`() {
        // Arrange
        val produtosFake = listOf(
            Produto(id = 1L, nome = "Produto 1", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("10.00"), categoria = categoriaFake, quantidadeEstoque = 100L),
            Produto(id = 2L, nome = "Produto 2", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("20.00"), categoria = categoriaFake, quantidadeEstoque = 50L),
            Produto(id = 3L, nome = "Produto 3", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("15.00"), categoria = categoriaFake, quantidadeEstoque = 75L)
        )
        every { produtoRepository.findAll() } returns produtosFake


        val atual: List<Produto> = produtoService.listarProdutos()


        Assertions.assertThat(atual).hasSize(produtosFake.size)
        Assertions.assertThat(atual).isEqualTo(produtosFake)
    }


    @Test
    fun `deve buscar um produto por ID`() {

        every { produtoRepository.findById(1L) } returns Optional.of(produtoFake)


        val atual: Produto = produtoService.getProdutoPorId(1L)


        Assertions.assertThat(atual).isNotNull
        Assertions.assertThat(atual).isEqualTo(produtoFake)
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

        every { produtoRepository.existsById(produtoFake.id!!) } returns true
        every { produtoRepository.deleteById(produtoFake.id!!) } returns Unit


        produtoService.deletarProduto(produtoFake.id!!)


        verify { produtoRepository.deleteById(produtoFake.id!!) }
    }


    @Test
    fun `deve lançar NotFoundException ao deletar produto inexistente`() {

        every { produtoRepository.existsById(produtoFake.id!!) } returns false


        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { produtoService.deletarProduto(produtoFake.id!!) }
            .withMessage("Produto não encontrado")

        verify(exactly = 1) { produtoRepository.existsById(produtoFake.id!!) }
        verify(exactly = 0) { produtoRepository.deleteById(any()) }
    }

    @Test
    fun `deve atualizar um produto existente`() {
        // Dado
        val categoriaExistente = categoriaFake.copy(id = 1L)
        every { categoriaRepository.findById(1L) } returns Optional.of(categoriaExistente)

        val produtoExistente = produtoFake.copy()
        every { produtoRepository.findById(produtoExistente.id!!) } returns Optional.of(produtoExistente)

        val updatedProdutoData = Produto(
            id = produtoExistente.id,
            nome = "Produto Atualizado",
            unidadeDeMedida = "Kilograma",
            precoUnitario = BigDecimal("50.00"),
            categoria = categoriaExistente,
            quantidadeEstoque = 20L
        )

        every { produtoRepository.save(any()) } returns updatedProdutoData

        val atual: Produto = produtoService.atualizarProduto(updatedProdutoData)

        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual).isEqualTo(updatedProdutoData)
    }

    @Test
    fun `deve lançar NotFoundException ao atualizar produto inexistente`() {

        every { produtoRepository.findById(1L) } returns Optional.empty()

        val updatedProdutoData = Produto(
            id = 1L,
            nome = "Produto Atualizado",
            unidadeDeMedida = "Kilograma",
            precoUnitario = BigDecimal("50.00"),
            categoria = Categoria(id = 2L, nome = "Roupas"),
            quantidadeEstoque = 20L
        )


        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { produtoService.atualizarProduto(updatedProdutoData) }
            .withMessage("Produto não encontrado")

        verify(exactly = 1) { produtoRepository.findById(1L) }
        verify(exactly = 0) { produtoRepository.save(any()) }
    }


    @Test
    fun `deve listar produtos por categoria`() {

        val categoriaId = 1L
        every { categoriaRepository.findById(categoriaId) } returns Optional.of(categoriaFake)

        val produtos = listOf(
            Produto(id = 1L, nome = "Produto 1", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.00"), categoria = categoriaFake, quantidadeEstoque = 10L),
            Produto(id = 2L, nome = "Produto 2", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("150.00"), categoria = categoriaFake, quantidadeEstoque = 15L),
            Produto(id = 3L, nome = "Produto 3", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("200.00"), categoria = categoriaFake, quantidadeEstoque = 20L)
        )
        every { produtoRepository.findByCategoria(categoriaFake) } returns produtos


        val listaDeProdutos: List<Produto> = produtoService.listarProdutosPorCategoria(categoriaId)


        Assertions.assertThat(listaDeProdutos).isNotNull()
        Assertions.assertThat(listaDeProdutos).hasSize(3)
        Assertions.assertThat(listaDeProdutos).isEqualTo(produtos)
    }


    @Test
    fun `deve atualizar o estoque de um produto existente`() {

        val quantidadeAtualizada = 5L
        val estoqueEsperado = quantidadeAtualizada + produtoFake.quantidadeEstoque
        val produtoEstoqueAtualizado = produtoFake.copy(quantidadeEstoque = estoqueEsperado)
        every { produtoRepository.findById(produtoFake.id!!) } returns Optional.of(produtoFake)
        every { produtoRepository.save(any()) } returns produtoEstoqueAtualizado


        val atual: Produto = produtoService.atualizarEstoque(produtoFake.id!!, quantidadeAtualizada)


        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual).isEqualTo(produtoEstoqueAtualizado)
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

        val quantidadeRemovida = 5L
        val estoqueEsperado = produtoFake.quantidadeEstoque - quantidadeRemovida
        val produtoEstoqueAtualizado = produtoFake.copy(quantidadeEstoque = estoqueEsperado)
        every { produtoRepository.findById(produtoFake.id!!) } returns Optional.of(produtoFake)
        every { produtoRepository.save(any()) } returns produtoEstoqueAtualizado


        val atual: Produto = produtoService.removerItensDoEstoque(produtoFake.id!!, quantidadeRemovida)


        Assertions.assertThat(atual).isNotNull()
        Assertions.assertThat(atual).isEqualTo(produtoEstoqueAtualizado)
    }


    @Test
    fun `deve lançar EstoqueInsuficienteException ao remover itens do estoque com quantidade maior que o estoque atual`() {

        val quantidadeRemovida = 20L
        every { produtoRepository.findById(produtoFake.id!!) } returns Optional.of(produtoFake)


        Assertions.assertThatExceptionOfType(EstoqueInsuficienteException::class.java)
            .isThrownBy { produtoService.removerItensDoEstoque(produtoFake.id!!, quantidadeRemovida) }
            .withMessage("Não há estoque suficiente para remover a quantidade desejada.")

        verify(exactly = 1) { produtoRepository.findById(produtoFake.id!!) }
        verify(exactly = 0) { produtoRepository.save(any()) }
    }
}