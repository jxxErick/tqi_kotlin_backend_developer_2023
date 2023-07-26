package com.grocery.jumarket.service

import com.grocery.jumarket.domain.*
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.ProdutoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.service.exception.EstoqueInsuficienteException
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.service.impl.CarrinhoService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CarrinhoServiceTest {
    @MockK
    lateinit var carrinhoRepository: CarrinhoRepository
    @MockK
    lateinit var produtoRepository: ProdutoRepository
    @MockK
    lateinit var usuarioRepository: UsuarioRepository

    lateinit var carrinhoService: CarrinhoService

    private lateinit var usuarioFake: Usuario
    private lateinit var produtoFake: Produto
    private lateinit var carrinhoFake: Carrinho
    private lateinit var categoriaFake: Categoria

    @BeforeEach
    fun setUp() {
        carrinhoService = CarrinhoService(carrinhoRepository, produtoRepository, usuarioRepository)
        usuarioFake = Usuario(
            id = 1L,
            email = "test@example.com",
            nome = "Jo",
            cpf = "12345678901",
            carrinho = null
        )
        categoriaFake = Categoria(id = 1L, nome = "Eletrônicos")
        produtoFake = Produto(
            id = 1L,
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.00"),
            categoria = categoriaFake,
            quantidadeEstoque = 10L
        )

        carrinhoFake = Carrinho(id = 1L, usuario = usuarioFake)

        every { carrinhoRepository.save(any()) } returns carrinhoFake
        every { carrinhoRepository.delete(carrinhoFake) } answers { nothing }
        every { carrinhoRepository.findAll() } returns emptyList()
    }

    @Test
    fun `deve adicionar um item ao carrinho`() {
        every { usuarioRepository.findById(any()) } returns Optional.of(usuarioFake)
        every { produtoRepository.findById(any()) } returns Optional.of(produtoFake)
        every { carrinhoRepository.save(any()) } returns carrinhoFake

        val quantidade = 2L
        carrinhoService.adicionarItemAoCarrinho(usuarioFake.id!!, produtoFake.id!!, quantidade)

        Assertions.assertEquals(1, carrinhoFake.itens.size)
        Assertions.assertEquals(quantidade, carrinhoFake.quantidadeItens)
        Assertions.assertEquals(BigDecimal("200.00"), carrinhoFake.valorTotal)
        verify(exactly = 1) { usuarioRepository.findById(any()) }
        verify(exactly = 1) { produtoRepository.findById(any()) }
        verify(exactly = 2) { carrinhoRepository.save(any()) }
    }
    @Test
    fun `deve lançar EstoqueInsuficienteException ao tentar adicionar produto sem estoque suficiente`() {
        val usuarioId = 1L
        val produtoId = 1L
        val quantidade = 10L

        every { usuarioRepository.findById(usuarioId) } returns Optional.of(usuarioFake)
        every { produtoRepository.findById(produtoId) } returns Optional.of(produtoFake)
        every { carrinhoRepository.save(any()) } answers { firstArg() }
        produtoFake.quantidadeEstoque = 5L

        assertThrows<EstoqueInsuficienteException> {
            carrinhoService.adicionarItemAoCarrinho(usuarioId, produtoId, quantidade)
        }
        verify(exactly = 1) { usuarioRepository.findById(usuarioId) }
        verify(exactly = 1) { produtoRepository.findById(produtoId) }
        verify(exactly = 0) { carrinhoRepository.save(any()) }
    }

    @Test
    fun `deve remover um item do carrinho`() {
        every { carrinhoRepository.findById(1) } returns Optional.of(carrinhoFake)
        every { carrinhoRepository.save(any()) } returns carrinhoFake
        every { carrinhoRepository.delete(any()) } just runs

        val itemId = 1L
        val itemToRemove = ItemCarrinho(
            id = itemId,
            produto = produtoFake,
            quantidade = 2L,
            precoUnitario = BigDecimal("100.00"),
            carrinho = carrinhoFake
        )
        carrinhoFake.itens.add(itemToRemove)
        carrinhoService.removerItemDoCarrinho(carrinhoFake, itemId)

        Assertions.assertTrue(carrinhoRepository.findAll().isEmpty())
        verify(exactly = 1) { carrinhoRepository.delete(any()) }
    }

    @Test
    fun `deve lançar NotFoundException ao tentar remover item inexistente`() {
        val itemId = 1L

        every { carrinhoRepository.findById(carrinhoFake.id!!) } returns Optional.of(carrinhoFake)

        Assertions.assertThrows(NotFoundException::class.java) {
            carrinhoService.removerItemDoCarrinho(carrinhoFake, itemId)
        }

    }
    @Test
    fun `deve listar os itens do carrinho por usuário`() {
        val item1 = ItemCarrinho(
            id = 1L,
            produto = produtoFake,
            quantidade = 2L,
            precoUnitario = BigDecimal("100.00"),
            carrinho = carrinhoFake
        )
        val item2 = ItemCarrinho(
            id = 2L,
            produto = produtoFake,
            quantidade = 3L,
            precoUnitario = BigDecimal("100.00"),
            carrinho = carrinhoFake
        )
        carrinhoFake.itens.add(item1)
        carrinhoFake.itens.add(item2)
        usuarioFake.carrinho = carrinhoFake

        every { usuarioRepository.findById(usuarioFake.id!!) } returns Optional.of(usuarioFake)
        val itensCarrinho = carrinhoService.listarItensPorUsuario(usuarioFake.id!!)

        Assertions.assertEquals(2, itensCarrinho.size)
        Assertions.assertTrue(itensCarrinho.contains(item1))
        Assertions.assertTrue(itensCarrinho.contains(item2))
        verify(exactly = 1) { usuarioRepository.findById(usuarioFake.id!!) }
    }

    @Test
    fun `deve lançar NotFoundException ao tentar listar itens de um usuário sem carrinho`() {
        every { usuarioRepository.findById(usuarioFake.id!!) } returns Optional.empty()

        Assertions.assertThrows(NotFoundException::class.java) {
            carrinhoService.listarItensPorUsuario(usuarioFake.id!!)
        }

        verify(exactly = 1) { usuarioRepository.findById(usuarioFake.id!!) }
    }

    @Test
    fun `deve deletar o carrinho de um usuário`() {
        usuarioFake.carrinho = carrinhoFake

        every { usuarioRepository.findById(usuarioFake.id!!) } returns Optional.of(usuarioFake)
        every { carrinhoRepository.delete(any()) } just runs
        carrinhoService.deletarCarrinho(usuarioFake.id!!)

        verify(exactly = 1) { usuarioRepository.findById(usuarioFake.id!!) }
        verify(exactly = 1) { carrinhoRepository.delete(any()) }
    }

    @Test
    fun `deve lançar NotFoundException ao tentar deletar carrinho de um usuário sem carrinho`() {
        every { usuarioRepository.findById(usuarioFake.id!!) } returns Optional.empty()

        Assertions.assertThrows(NotFoundException::class.java) {
            carrinhoService.deletarCarrinho(usuarioFake.id!!)
        }

        verify(exactly = 1) { usuarioRepository.findById(usuarioFake.id!!) }
    }

    @Test
    fun `deve retornar o carrinho de um usuário`() {
        usuarioFake.carrinho = carrinhoFake

        every { usuarioRepository.findById(usuarioFake.id!!) } returns Optional.of(usuarioFake)
        val carrinho = carrinhoService.getCarrinhoPorUsuario(usuarioFake.id!!)

        Assertions.assertEquals(carrinhoFake, carrinho)
        verify(exactly = 1) { usuarioRepository.findById(usuarioFake.id!!) }
    }

    @Test
    fun `deve lançar NotFoundException ao tentar retornar carrinho de um usuário sem carrinho`() {
        val usuarioFakeSemCarrinho = Usuario(
            id = 2L,
            email = "test@exemplo.com",
            nome = "Jo",
            cpf = "12345678900",
            carrinho = null
        )

        every { usuarioRepository.findById(usuarioFakeSemCarrinho.id!!) } returns Optional.of(usuarioFakeSemCarrinho)

        val exception = assertThrows<NotFoundException> {
           carrinhoService.getCarrinhoPorUsuario(usuarioFakeSemCarrinho.id!!)
        }
        Assertions.assertEquals("Carrinho do usuario vazio", exception.message)
    }
}