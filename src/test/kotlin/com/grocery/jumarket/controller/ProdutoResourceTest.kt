package com.grocery.jumarket.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.grocery.jumarket.JuMarketApplication
import com.grocery.jumarket.dto.NewProdutoDTO
import com.grocery.jumarket.dto.ProdutoDTO
import com.grocery.jumarket.service.ProdutoService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal


@SpringBootTest(classes = [JuMarketApplication::class])
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class ProdutoResourceTest {
    @MockK private lateinit var produtoService: ProdutoService
    @InjectMockKs private lateinit var produtoResource: ProdutoResource
    private val objectMapper = ObjectMapper()
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        produtoService = mockk()
        produtoResource = ProdutoResource(produtoService)
        mockMvc = MockMvcBuilders.standaloneSetup(produtoResource).build()
        every { produtoService.deletarProduto(any()) } just Runs
    }
    @Test
    fun `deve criar um novo produto`() {

        val produtoDTO = NewProdutoDTO(
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.0"),
            categoriaId = 1L,
            quantidadeEstoque = 0L
        )
        val produtoCriado = ProdutoDTO(
            id = 1L,
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.0"),
            categoriaId = 1L,
            quantidadeEstoque = 0L
        )

        every { produtoService.criarProduto(produtoDTO) } returns produtoCriado

        val request = post("/api/produtos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(produtoDTO))
        val result = MockMvcBuilders.standaloneSetup(produtoResource).build().perform(request)

        result.andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(produtoCriado.id))
            .andExpect(jsonPath("$.nome").value(produtoCriado.nome))
            .andExpect(jsonPath("$.unidadeDeMedida").value(produtoCriado.unidadeDeMedida))
            .andExpect(jsonPath("$.precoUnitario").value(produtoCriado.precoUnitario))
            .andExpect(jsonPath("$.categoriaId").value(produtoCriado.categoriaId))
            .andExpect(jsonPath("$.quantidadeEstoque").value(produtoCriado.quantidadeEstoque))
    }

    @Test
    fun `deve listar todos os produtos`() {

        val produtos = listOf(
            ProdutoDTO(id = 1L, nome = "Produto 1", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("100.0"), categoriaId = 1L, quantidadeEstoque = 0L),
            ProdutoDTO(id = 2L, nome = "Produto 2", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("150.0"), categoriaId = 2L, quantidadeEstoque = 0L),
            ProdutoDTO(id = 3L, nome = "Produto 3", unidadeDeMedida = "Unidade", precoUnitario = BigDecimal("200.0"), categoriaId = 3L, quantidadeEstoque = 0L)
        )

        every { produtoService.listarProdutos() } returns produtos


        val request = get("/api/produtos")
        val result = MockMvcBuilders.standaloneSetup(produtoResource).build().perform(request)

        result.andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(produtos.size))
            .andExpect(jsonPath("$[0].id").value(produtos[0].id))
            .andExpect(jsonPath("$[0].nome").value(produtos[0].nome))
            .andExpect(jsonPath("$[0].unidadeDeMedida").value(produtos[0].unidadeDeMedida))
            .andExpect(jsonPath("$[0].precoUnitario").value(produtos[0].precoUnitario))
            .andExpect(jsonPath("$[0].categoriaId").value(produtos[0].categoriaId))
            .andExpect(jsonPath("$[0].quantidadeEstoque").value(produtos[0].quantidadeEstoque))
            .andExpect(jsonPath("$[1].id").value(produtos[1].id))
            .andExpect(jsonPath("$[1].nome").value(produtos[1].nome))
            .andExpect(jsonPath("$[1].unidadeDeMedida").value(produtos[1].unidadeDeMedida))
            .andExpect(jsonPath("$[1].precoUnitario").value(produtos[1].precoUnitario))
            .andExpect(jsonPath("$[1].categoriaId").value(produtos[1].categoriaId))
            .andExpect(jsonPath("$[1].quantidadeEstoque").value(produtos[1].quantidadeEstoque))
            .andExpect(jsonPath("$[2].id").value(produtos[2].id))
            .andExpect(jsonPath("$[2].nome").value(produtos[2].nome))
            .andExpect(jsonPath("$[2].unidadeDeMedida").value(produtos[2].unidadeDeMedida))
            .andExpect(jsonPath("$[2].precoUnitario").value(produtos[2].precoUnitario))
            .andExpect(jsonPath("$[2].categoriaId").value(produtos[2].categoriaId))
            .andExpect(jsonPath("$[2].quantidadeEstoque").value(produtos[2].quantidadeEstoque))
    }
    @Test
    fun `deve buscar um produto por ID`() {

        val produtoId = 1L
        val produtoDTO = ProdutoDTO(
            id = produtoId,
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.0"),
            categoriaId = 1L,
            quantidadeEstoque = 10L
        )

        every { produtoService.getProdutoPorId(produtoId) } returns produtoDTO

        mockMvc.perform(get("/api/produtos/$produtoId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(produtoDTO.id))
            .andExpect(jsonPath("$.nome").value(produtoDTO.nome))
            .andExpect(jsonPath("$.unidadeDeMedida").value(produtoDTO.unidadeDeMedida))
            .andExpect(jsonPath("$.precoUnitario").value(produtoDTO.precoUnitario))
            .andExpect(jsonPath("$.categoriaId").value(produtoDTO.categoriaId))
            .andExpect(jsonPath("$.quantidadeEstoque").value(produtoDTO.quantidadeEstoque))
    }
    @Test
    fun `deve atualizar um produto existente`() {

        val produtoId = 1L
        val newProdutoDTO = NewProdutoDTO(
            nome = "Produto Atualizado",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("120.0"),
            categoriaId = 2L,
            quantidadeEstoque = 30L
        )

        val produtoAtualizado = ProdutoDTO(
            id = produtoId,
            nome = "Produto Atualizado",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("120.0"),
            categoriaId = 2L,
            quantidadeEstoque = 30L
        )

        every { produtoService.atualizarProduto(produtoId, newProdutoDTO) } returns produtoAtualizado

        mockMvc.perform(
            put("/api/produtos/$produtoId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProdutoDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(produtoAtualizado.id))
            .andExpect(jsonPath("$.nome").value(produtoAtualizado.nome))
            .andExpect(jsonPath("$.unidadeDeMedida").value(produtoAtualizado.unidadeDeMedida))
            .andExpect(jsonPath("$.precoUnitario").value(produtoAtualizado.precoUnitario))
            .andExpect(jsonPath("$.categoriaId").value(produtoAtualizado.categoriaId))
            .andExpect(jsonPath("$.quantidadeEstoque").value(produtoAtualizado.quantidadeEstoque))
    }

    @Test
    fun `deve deletar um produto existente`() {

        val produtoId = 1L

        mockMvc.perform(delete("/api/produtos/$produtoId"))
            .andExpect(status().isNoContent)


        verify(exactly = 1) { produtoService.deletarProduto(produtoId) }
    }

    @Test
    fun `deve listar produtos por categoria`() {
        val categoriaId = 1L
        val produto1 = ProdutoDTO(
            id = 1L,
            nome = "Produto 1",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("50.0"),
            categoriaId = categoriaId,
            quantidadeEstoque = 20L
        )

        val produto2 = ProdutoDTO(
            id = 2L,
            nome = "Produto 2",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("30.0"),
            categoriaId = categoriaId,
            quantidadeEstoque = 15L
        )

        val produtos = listOf(produto1, produto2)

        every { produtoService.listarProdutosPorCategoria(categoriaId) } returns produtos

        mockMvc.perform(get("/api/produtos/categoria/$categoriaId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(produto1.id))
            .andExpect(jsonPath("$[0].nome").value(produto1.nome))
            .andExpect(jsonPath("$[0].unidadeDeMedida").value(produto1.unidadeDeMedida))
            .andExpect(jsonPath("$[0].precoUnitario").value(produto1.precoUnitario))
            .andExpect(jsonPath("$[0].categoriaId").value(produto1.categoriaId))
            .andExpect(jsonPath("$[0].quantidadeEstoque").value(produto1.quantidadeEstoque))
            .andExpect(jsonPath("$[1].id").value(produto2.id))
            .andExpect(jsonPath("$[1].nome").value(produto2.nome))
            .andExpect(jsonPath("$[1].unidadeDeMedida").value(produto2.unidadeDeMedida))
            .andExpect(jsonPath("$[1].precoUnitario").value(produto2.precoUnitario))
            .andExpect(jsonPath("$[1].categoriaId").value(produto2.categoriaId))
            .andExpect(jsonPath("$[1].quantidadeEstoque").value(produto2.quantidadeEstoque))
    }

    @Test
    fun `deve atualizar o estoque de um produto existente`() {

        val produtoId = 1L
        val quantidade = 5L

        val produtoAtualizado = ProdutoDTO(
            id = produtoId,
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.0"),
            categoriaId = 1L,
            quantidadeEstoque = 15L
        )

        every { produtoService.atualizarEstoque(produtoId, quantidade) } returns produtoAtualizado

        mockMvc.perform(
            put("/api/produtos/$produtoId/estoque/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(quantidade.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(produtoAtualizado.id))
            .andExpect(jsonPath("$.nome").value(produtoAtualizado.nome))
            .andExpect(jsonPath("$.unidadeDeMedida").value(produtoAtualizado.unidadeDeMedida))
            .andExpect(jsonPath("$.precoUnitario").value(produtoAtualizado.precoUnitario))
            .andExpect(jsonPath("$.categoriaId").value(produtoAtualizado.categoriaId))
            .andExpect(jsonPath("$.quantidadeEstoque").value(produtoAtualizado.quantidadeEstoque))
    }

    @Test
    fun `deve remover itens do estoque de um produto existente`() {

        val produtoId = 1L
        val quantidade = 5L

        val produtoAtualizado = ProdutoDTO(
            id = produtoId,
            nome = "Produto Teste",
            unidadeDeMedida = "Unidade",
            precoUnitario = BigDecimal("100.0"),
            categoriaId = 1L,
            quantidadeEstoque = 5L
        )

        every { produtoService.removerItensDoEstoque(produtoId, quantidade) } returns produtoAtualizado

        mockMvc.perform(
            put("/api/produtos/$produtoId/estoque/remover")
                .param("quantidade", quantidade.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(produtoAtualizado.id))
            .andExpect(jsonPath("$.nome").value(produtoAtualizado.nome))
            .andExpect(jsonPath("$.unidadeDeMedida").value(produtoAtualizado.unidadeDeMedida))
            .andExpect(jsonPath("$.precoUnitario").value(produtoAtualizado.precoUnitario))
            .andExpect(jsonPath("$.categoriaId").value(produtoAtualizado.categoriaId))
            .andExpect(jsonPath("$.quantidadeEstoque").value(produtoAtualizado.quantidadeEstoque))
    }

}