package com.grocery.jumarket.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.grocery.jumarket.JuMarketApplication
import com.grocery.jumarket.domain.Usuario
import com.grocery.jumarket.domain.Venda
import com.grocery.jumarket.dto.request.DataVendaDTO
import com.grocery.jumarket.dto.request.FinalizarVendaDTO
import com.grocery.jumarket.ennumeration.FormaDePagamento
import com.grocery.jumarket.service.impl.VendaService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest(classes = [JuMarketApplication::class])
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class VendaResourceTest {

    @MockK
    private lateinit var vendaService: VendaService

    @InjectMockKs
    private lateinit var vendaResource: VendaResource

    private val objectMapper = ObjectMapper()
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        vendaService = mockk(relaxed = true)
        vendaResource = VendaResource(vendaService)
        mockMvc = MockMvcBuilders.standaloneSetup(vendaResource).build()
    }

    @Test
    fun `deve finalizar a venda com sucesso`() {
        val finalizarVendaDTO = FinalizarVendaDTO(formaDePagamento = "CARTAO_DE_CREDITO", usuarioId = 1L)

        val usuario = Usuario(
            id = 1L,
            email = "test@example.com",
            nome = "Jo",
            cpf = "12345678901",
            carrinho = null
        )

        val venda = Venda(
            id = 1L,
            usuario = usuario,
            valorTotal = BigDecimal("370.00"),
            formaDePagamento = FormaDePagamento.CARTAO_DE_CREDITO,
            dataVenda = LocalDate.now()
        )

        every { vendaService.finalizarVenda(any(), any()) } returns venda

        val request = post("/api/venda/finalizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(finalizarVendaDTO))

        mockMvc.perform(request)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(venda.id))
            .andExpect(jsonPath("$.usuario.id").value(venda.usuario.id))
            .andExpect(jsonPath("$.usuario.email").value(venda.usuario.email))
            .andExpect(jsonPath("$.valorTotal").value(venda.valorTotal.toDouble()))
            .andExpect(jsonPath("$.formaDePagamento").value(venda.formaDePagamento.name))
    }
}