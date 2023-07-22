package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.service.impl.CategoriaService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.util.*


@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CategoriaServiceTest {

    @MockK lateinit var categoriaRepository: CategoriaRepository
    @InjectMockKs lateinit var categoriaService: CategoriaService
    private lateinit var categoria: Categoria
    @BeforeEach
    fun setUp() {
        categoriaRepository = mockk()
        categoriaService = CategoriaService(categoriaRepository)

        categoria = Categoria(
            id = 1L,
            nome = "Categoria Teste"
        )
    }

    @Test
    fun `deve criar uma nova categoria`() {
        every { categoriaRepository.save(any()) } returns categoria

        val resultado = categoriaService.criarCategoria(categoria)

        assertEquals(categoria, resultado)
    }

    @Test
    fun `deve listar as categorias`() {
        val listaCategorias = listOf(categoria)
        every { categoriaRepository.findAll() } returns listaCategorias

        val resultado = categoriaService.listarCategorias()

        assertEquals(listaCategorias, resultado)
    }

    @Test
    fun `deve deletar uma categoria`() {
        every { categoriaRepository.findById(categoria.id!!) } returns Optional.of(categoria)
        every { categoriaRepository.delete(categoria) } just Runs

        categoriaService.deletarCategoria(categoria.id!!)

        verify(exactly = 1) { categoriaRepository.delete(categoria) }
    }
    @Test
    fun `deve editar uma categoria`() {
        val novaDescricao = "Nova Categoria"
        val categoriaAtualizada = categoria.copy(nome = novaDescricao)
        every { categoriaRepository.findById(categoria.id!!) } returns Optional.of(categoria)
        every { categoriaRepository.save(categoria) } returns categoriaAtualizada

        val resultado = categoriaService.editarCategoria(categoria.id!!, categoriaAtualizada)

        assertEquals(categoriaAtualizada, resultado)
    }

    @Test
    fun `deve lançar NotFoundException ao tentar editar categoria inexistente`() {
        val categoriaInexistente = Categoria(nome = "t", id = 2L)
        every { categoriaRepository.findById(categoriaInexistente.id!!) } returns Optional.empty()

        assertThrows(NotFoundException::class.java) { categoriaService.editarCategoria(categoriaInexistente.id!!, categoriaInexistente) }
    }

    @Test
    fun `deve buscar categoria pelo ID`() {
        val idCategoria = 1L
        every { categoriaRepository.findById(idCategoria) } returns Optional.of(categoria)

        val resultado = categoriaService.buscarCategoriaPorId(idCategoria)

        assertEquals(categoria, resultado)
    }

    @Test
    fun `deve lançar NotFoundException ao tentar buscar categoria por ID inexistente`() {
        val idCategoriaNaoExistente = 2L
        every { categoriaRepository.findById(idCategoriaNaoExistente) } returns Optional.empty()

        assertThrows(NotFoundException::class.java) { categoriaService.buscarCategoriaPorId(idCategoriaNaoExistente) }
    }
}