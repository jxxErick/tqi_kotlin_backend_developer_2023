import com.fasterxml.jackson.databind.ObjectMapper
import com.grocery.jumarket.JuMarketApplication
import com.grocery.jumarket.controller.CategoriaResource
import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.request.CategoriaDTO
import com.grocery.jumarket.dto.view.CategoriaViewDTO
import com.grocery.jumarket.service.impl.CategoriaService
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

@SpringBootTest(classes = [JuMarketApplication::class])
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CategoriaResourceTest {

    @MockK private lateinit var categoriaService: CategoriaService
    @InjectMockKs private lateinit var categoriaResource: CategoriaResource
    private val objectMapper = ObjectMapper()
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        categoriaService = mockk(relaxed = true)
        categoriaResource = CategoriaResource(categoriaService)
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaResource).build()
        every { categoriaService.deletarCategoria(any()) } just Runs
    }

    @Test
    fun `deve criar uma nova categoria`() {
        val categoriaDTO = CategoriaDTO(nome = "Categoria Teste")
        val categoriaCriada = CategoriaViewDTO(id = 1L, nome = categoriaDTO.nome)
        val categoria = Categoria(id = categoriaCriada.id, nome = categoriaCriada.nome)

        every { categoriaService.criarCategoria(any()) } returns categoria

        val request = post("/api/categorias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoriaDTO))
        val result = mockMvc.perform(request)

        result.andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(categoriaCriada.id))
            .andExpect(jsonPath("$.nome").value(categoriaCriada.nome))
    }

    @Test
    fun `deve listar todas as categorias`() {
        val categorias = listOf(
            Categoria(id = 1L, nome = "Categoria 1"),
            Categoria(id = 2L, nome = "Categoria 2"),
            Categoria(id = 3L, nome = "Categoria 3")
        )

        every { categoriaService.listarCategorias() } returns categorias

        val request = get("/api/categorias")
        val result = mockMvc.perform(request)

        result.andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(categorias.size))
            .andExpect(jsonPath("$[0].id").value(categorias[0].id))
            .andExpect(jsonPath("$[0].nome").value(categorias[0].nome))
            .andExpect(jsonPath("$[1].id").value(categorias[1].id))
            .andExpect(jsonPath("$[1].nome").value(categorias[1].nome))
            .andExpect(jsonPath("$[2].id").value(categorias[2].id))
            .andExpect(jsonPath("$[2].nome").value(categorias[2].nome))
    }

    @Test
    fun `deve buscar uma categoria por ID`() {
        val categoriaId = 1L
        val categoria = Categoria(id = categoriaId, nome = "Categoria Teste")

        every { categoriaService.buscarCategoriaPorId(categoriaId) } returns categoria

        mockMvc.perform(get("/api/categorias/$categoriaId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(categoria.id))
            .andExpect(jsonPath("$.nome").value(categoria.nome))
    }

    @Test
    fun `deve deletar uma categoria existente`() {
        val categoriaId = 1L

        mockMvc.perform(delete("/api/categorias/$categoriaId"))
            .andExpect(status().isNoContent)

        verify(exactly = 1) { categoriaService.deletarCategoria(categoriaId) }
    }

    @Test
    fun `deve editar uma categoria existente`() {
        val categoriaId = 1L
        val categoriaDTO = CategoriaDTO(nome = "Categoria Atualizada")
        val categoriaAtualizada = Categoria(id = categoriaId, nome = categoriaDTO.nome)

        every { categoriaService.editarCategoria(categoriaId, any()) } returns categoriaAtualizada

        mockMvc.perform(
            patch("/api/categorias/$categoriaId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(categoriaAtualizada.id))
            .andExpect(jsonPath("$.nome").value(categoriaAtualizada.nome))
    }
}