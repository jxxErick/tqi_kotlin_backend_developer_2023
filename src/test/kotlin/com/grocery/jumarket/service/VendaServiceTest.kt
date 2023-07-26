import com.grocery.jumarket.domain.*
import com.grocery.jumarket.ennumeration.FormaDePagamento
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.repositories.VendaRepository
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.EstoqueInsuficienteException
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.service.impl.VendaService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*



@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class VendaServiceTest {
    @MockK
    lateinit var vendaRepository: VendaRepository

    @MockK
    lateinit var usuarioRepository: UsuarioRepository

    @MockK
    lateinit var carrinhoRepository: CarrinhoRepository


    lateinit var vendaService: VendaService

    private lateinit var usuarioFake: Usuario
    private lateinit var produtoFake: Produto
    private lateinit var carrinhoFake: Carrinho
    private lateinit var categoriaFake: Categoria

    @BeforeEach
    fun setUp() {
        vendaService = VendaService(vendaRepository, usuarioRepository, carrinhoRepository)

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

        every { vendaRepository.save(any()) } answers { firstArg() }
        every { carrinhoRepository.save(any()) } answers { firstArg() }
        every { carrinhoRepository.delete(any()) } just runs
        every { carrinhoRepository.findAll() } returns emptyList()
    }

    @Test
    fun `deve finalizar a venda com sucesso`() {
        usuarioFake.carrinho = Carrinho(usuario = usuarioFake)
        val quantidadeItem1 = 2L
        val quantidadeItem2 = 3L
        val item1 = ItemCarrinho(produtoFake, quantidadeItem1, produtoFake.precoUnitario)
        val item2 = ItemCarrinho(produtoFake, quantidadeItem2, produtoFake.precoUnitario)
        usuarioFake.carrinho?.itens?.addAll(listOf(item1, item2))

        every { usuarioRepository.findById(any()) } returns Optional.of(usuarioFake)
        every { carrinhoRepository.save(any()) } answers {
            val carrinho = arg<Carrinho>(0)
            usuarioFake.carrinho = carrinho
            carrinho
        }
        val formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO
        val venda = vendaService.finalizarVenda(formaDePagamento, usuarioFake.id!!)

        Assertions.assertEquals(usuarioFake, venda.usuario)
        Assertions.assertEquals(formaDePagamento, venda.formaDePagamento)
        val valorTotalEsperado = quantidadeItem1.toBigDecimal() * produtoFake.precoUnitario +
                quantidadeItem2.toBigDecimal() * produtoFake.precoUnitario
        Assertions.assertEquals(valorTotalEsperado, venda.valorTotal)
        Assertions.assertTrue(carrinhoFake.itens.isEmpty())

    }
    @Test
    fun `deve lançar NotFoundException ao tentar finalizar venda de usuário inexistente`() {
        every { usuarioRepository.findById(any()) } returns Optional.empty()
        val formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO

        val exception = assertThrows<NotFoundException> {
            vendaService.finalizarVenda(formaDePagamento, 1L)
        }
        assertEquals("Usuário não encontrado", exception.message)
    }

    @Test
    fun `deve lançar NotFoundException ao tentar finalizar venda com carrinho vazio`() {
        usuarioFake.carrinho = Carrinho(usuario = usuarioFake)
        every { usuarioRepository.findById(any()) } returns Optional.of(usuarioFake)
        val formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO

       val exception = assertThrows<BusinessException> {
            vendaService.finalizarVenda(formaDePagamento, usuarioFake.id!!)
        }
        assertEquals("Carrinho Vazio.", exception.message)
    }

    @Test
    fun `deve lançar EstoqueInsuficienteException ao tentar finalizar venda com estoque insuficiente`() {
        usuarioFake.carrinho = Carrinho(usuario = usuarioFake)

        every { usuarioRepository.findById(any()) } returns Optional.of(usuarioFake)
        val quantidadeItemExcedente = produtoFake.quantidadeEstoque + 1L
        usuarioFake.carrinho?.itens?.addAll(listOf(ItemCarrinho(produtoFake, quantidadeItemExcedente, produtoFake.precoUnitario)))
        val formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO

        val exception = assertThrows<EstoqueInsuficienteException> {
            vendaService.finalizarVenda(formaDePagamento, usuarioFake.id!!)
        }
        assertEquals("Não há estoque suficiente para concluir a venda.", exception.message)
    }

    @Test
    fun `deve retornar lista de vendas quando o repositório contém dados`() {
        val venda1 = Venda(id = 1L, usuario = usuarioFake, valorTotal = BigDecimal("250.00"), formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO)
        val venda2 = Venda(id = 2L, usuario = usuarioFake, valorTotal = BigDecimal("120.50"), formaDePagamento = FormaDePagamento.DINHEIRO)

        every { vendaRepository.findAll() } returns listOf(venda1, venda2)
        val listaDeVendas = vendaService.listarVendas()

        assertFalse(listaDeVendas.isEmpty())
        assertTrue(listaDeVendas.contains(venda1))
        assertTrue(listaDeVendas.contains(venda2))
    }

    @Test
    fun `deve retornar lista de vendas por data quando o repositório contém dados`() {
        val dataVenda1 = LocalDate.of(2023, 1, 1)
        val dataVenda2 = LocalDate.of(2023, 1, 2)
        val venda1 = Venda(id = 1L, usuario = usuarioFake, valorTotal = BigDecimal("250.00"), formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO, dataVenda = dataVenda1)
        val venda2 = Venda(id = 2L, usuario = usuarioFake, valorTotal = BigDecimal("120.50"), formaDePagamento = FormaDePagamento.DINHEIRO, dataVenda = dataVenda2)


        every { vendaRepository.findAllByDataVenda(dataVenda1) } returns listOf(venda1)
        every { vendaRepository.findAllByDataVenda(dataVenda2) } returns listOf(venda2)


        val listaDeVendasData1 = vendaService.listarVendasPorData(dataVenda1)
        assertFalse(listaDeVendasData1.isEmpty())
        assertTrue(listaDeVendasData1.contains(venda1))
        assertFalse(listaDeVendasData1.contains(venda2))


        val listaDeVendasData2 = vendaService.listarVendasPorData(dataVenda2)
        assertFalse(listaDeVendasData2.isEmpty())
        assertTrue(listaDeVendasData2.contains(venda2))
        assertFalse(listaDeVendasData2.contains(venda1))
    }
}

