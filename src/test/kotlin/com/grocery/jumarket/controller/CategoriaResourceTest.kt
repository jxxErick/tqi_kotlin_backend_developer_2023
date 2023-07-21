import com.fasterxml.jackson.databind.ObjectMapper
import com.grocery.jumarket.JuMarketApplication
import com.grocery.jumarket.controller.CategoriaResource
import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.CategoriaDTO
import com.grocery.jumarket.dto.NewCategoriaDTO
import com.grocery.jumarket.service.impl.CategoriaService
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
@SpringBootTest(classes = [JuMarketApplication::class])
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CategoriaResourceTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var categoriaService: CategoriaService

    @BeforeEach
    fun setUp() {
        categoriaService = mockk()
        val categoriaResource = CategoriaResource(categoriaService)
        mockMvc = standaloneSetup(categoriaResource).build()
    }


    @Test
    fun `deve criar uma nova categoria`() {

        val categoriaDTO = CategoriaDTO(id = 1L, nome = "Eletrônicos")

        every { categoriaService.criarCategoria(any()) } returns categoriaDTO

        mockMvc.perform(post("/api/categorias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(categoriaDTO)))
            .andExpect(status().isCreated)
            .andExpect(content().json(ObjectMapper().writeValueAsString(categoriaDTO)))
    }

    @Test
    fun `deve listar todas as categorias`() {

        val categorias = listOf(
            CategoriaDTO(id = 1L, nome = "Eletrônicos"),
            CategoriaDTO(id = 2L, nome = "Roupas"),
            CategoriaDTO(id = 3L, nome = "Alimentos")
        )

        every { categoriaService.listarCategorias() } returns categorias

        mockMvc.perform(get("/api/categorias"))
            .andExpect(status().isOk)
            .andExpect(content().json(ObjectMapper().writeValueAsString(categorias)))
    }

    @Test
    fun `deve buscar uma categoria por id`() {

        val categoriaDTO = CategoriaDTO(id = 1L, nome = "Eletrônicos")

        every { categoriaService.buscarCategoriaPorId(1L) } returns categoriaDTO

        mockMvc.perform(get("/api/categorias/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(ObjectMapper().writeValueAsString(categoriaDTO)))
    }



    @Test
    fun `deve editar uma categoria`() {

        val categoriaDTO = CategoriaDTO(id = 1L, nome = "Eletrônicos Atualizados")
        val newCategoriaDTO = NewCategoriaDTO(nome = "Eletrônicos Atualizados")
        val categoriaAtualizada = Categoria(id = 1L, nome = "Eletrônicos Atualizados")

        every { categoriaService.editarCategoria(1L, newCategoriaDTO) } returns categoriaAtualizada

        mockMvc.perform(put("/api/categorias/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(newCategoriaDTO)))
            .andExpect(status().isOk)
            .andExpect(content().json(ObjectMapper().writeValueAsString(categoriaDTO)))
    }

    @Test
    fun `deve deletar uma categoria`() {

        val idCategoria = 1L

        every { categoriaService.deletarCategoria(idCategoria) } just Runs

        mockMvc.perform(delete("/api/categorias/$idCategoria")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
    }

}