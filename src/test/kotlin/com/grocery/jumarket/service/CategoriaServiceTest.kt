package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.CategoriaDTO
import com.grocery.jumarket.dto.NewCategoriaDTO
import com.grocery.jumarket.repositories.CategoriaRepository
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.service.impl.CategoriaService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CategoriaServiceTest {
@MockK lateinit var categoriaRepository: CategoriaRepository
@InjectMockKs lateinit var categoriaService: CategoriaService

    @Test
    fun `deve criar uma nova categoria`() {

        val categoriaFake: CategoriaDTO = construirCategoria()
        val categoriaCriada: Categoria = Categoria(id = categoriaFake.id, nome = categoriaFake.nome)

        every { categoriaRepository.save(any()) } returns categoriaCriada

        val atual: CategoriaDTO = categoriaService.criarCategoria(categoriaFake)

        assertThat(atual).isNotNull()
        assertThat(atual).isExactlyInstanceOf(CategoriaDTO::class.java)
        assertThat(atual.id).isEqualTo(categoriaCriada.id)
        assertThat(atual.nome).isEqualTo(categoriaCriada.nome)
    }

    @Test
    fun `deve listar todas as categorias`() {

        val categoriasFake = listOf(
            Categoria(id = 1L, nome = "Eletrônicos"),
            Categoria(id = 2L, nome = "Roupas"),
            Categoria(id = 3L, nome = "Alimentos")
        )
        every { categoriaRepository.findAll() } returns categoriasFake

        val atual: List<CategoriaDTO> = categoriaService.listarCategorias()

        assertThat(atual).hasSize(categoriasFake.size)
        for (i in categoriasFake.indices) {
            assertThat(atual[i].id).isEqualTo(categoriasFake[i].id)
            assertThat(atual[i].nome).isEqualTo(categoriasFake[i].nome)
        }
    }

    @Test
    fun `deve buscar uma categoria passando a id`() {

        val idFake: Long = Random().nextLong()
        val categoriaFake: CategoriaDTO = construirCategoria(id = idFake)
        every { categoriaRepository.findById(idFake) } returns Optional.of(Categoria(id = categoriaFake.id,
            nome = categoriaFake.nome))

        val atual: CategoriaDTO = categoriaService.buscarCategoriaPorId(idFake)

        Assertions.assertThat(atual).isNotNull
        Assertions.assertThat(atual).isExactlyInstanceOf(CategoriaDTO::class.java)
        Assertions.assertThat(atual.id).isEqualTo(categoriaFake.id)
        Assertions.assertThat(atual.nome).isEqualTo(categoriaFake.nome)

    }
    @Test
    fun `deve lançar uma notFoundExcpetion caso a id seja invalida `(){

        val idFake: Long = Random().nextLong()
        every { categoriaRepository.findById(idFake) } returns Optional.empty()

        Assertions.assertThatExceptionOfType(NotFoundException::class.java)
            .isThrownBy { categoriaService.buscarCategoriaPorId(idFake) }
            .withMessage("Categoria não encontrada")

        verify ( exactly = 1 ) {categoriaRepository.findById(idFake)}
    }

    @Test
    fun `deve deletar uma categoria existente`() {

        val categoriaExistente = Categoria(id = 1L, nome = "Eletrônicos")
        every { categoriaRepository.findById(categoriaExistente.id!!) } returns Optional.of(categoriaExistente)
        every { categoriaRepository.deleteById(categoriaExistente.id!!) } returns Unit


        categoriaService.deletarCategoria(categoriaExistente.id!!)


        verify { categoriaRepository.deleteById(categoriaExistente.id!!) }
    }
    @Test
    fun `deve editar uma categoria existente`() {

        val categoriaExistente = Categoria(id = 1L, nome = "Eletrônicos")
        every { categoriaRepository.findById(categoriaExistente.id!!) } returns Optional.of(categoriaExistente)

        val newCategoriaDTO = NewCategoriaDTO(nome = "Eletrônicos Atualizados")
        val categoriaAtualizada = Categoria(id = 1L, nome = "Eletrônicos Atualizados")
        every { categoriaRepository.save(any()) } returns categoriaAtualizada

        val atual: Categoria = categoriaService.editarCategoria(categoriaExistente.id!!, newCategoriaDTO)

        assertThat(atual).isNotNull()
        assertThat(atual.id).isEqualTo(categoriaExistente.id)
        assertThat(atual.nome).isEqualTo(newCategoriaDTO.nome)
    }
}

private fun construirCategoria(
    nome: String = "Eletrônicos",
    id: Long = 1L
) = CategoriaDTO(
    nome = nome,
    id = id
)

