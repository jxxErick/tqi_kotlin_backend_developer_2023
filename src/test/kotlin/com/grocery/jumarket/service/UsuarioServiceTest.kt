package com.grocery.jumarket.service

import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.Usuario
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.NotFoundException
import com.grocery.jumarket.service.impl.UsuarioService
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.junit.jupiter.api.Test
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.testng.Assert.assertThrows
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class UsuarioServiceTest {

    @MockK
    private lateinit var usuarioRepository: UsuarioRepository

    @InjectMockKs
    private lateinit var usuarioService: UsuarioService

    private lateinit var usuario: Usuario
    private val carrinho: Carrinho? = null

    @BeforeEach
    fun setUp() {
        usuarioRepository = mockk(relaxed = true)
        usuarioService = UsuarioService(usuarioRepository)

        usuario = Usuario(
            id = 1L,
            email = "usuario@teste.com",
            nome = "Nome do Usuário",
            cpf = "12345678900",
            carrinho = carrinho
        )
    }

    @Test
    fun `Deve criar um novo usuário`() {

        every { usuarioRepository.findByEmail(usuario.email) } returns null
        every { usuarioRepository.findByCpf(usuario.cpf) } returns null
        every { usuarioRepository.save(any()) } returns usuario

        val resultado = usuarioService.criarUsuario(usuario)

        assertEquals(usuario, resultado)
    }

    @Test
    fun `Deve lançar BusinessException para e-mail já existente`() {

        every { usuarioRepository.findByEmail(usuario.email) } returns usuario

        assertThrows(BusinessException::class.java) { usuarioService.criarUsuario(usuario) }
    }

    @Test
    fun `Deve lançar BusinessException para CPF já existente`() {

        every { usuarioRepository.findByEmail(usuario.email) } returns null
        every { usuarioRepository.findByCpf(usuario.cpf) } returns usuario


        assertThrows(BusinessException::class.java) { usuarioService.criarUsuario(usuario) }
    }

    @Test
    fun `Deve retornar a lista de usuários`() {

        val listaUsuarios = listOf(usuario)
        every { usuarioRepository.findAll() } returns listaUsuarios

        val resultado = usuarioService.listarUsuarios()

        assertEquals(listaUsuarios, resultado)
    }

    @Test
    fun `Deve retornar o usuário pelo ID`() {

        val idUsuario = 1L
        every { usuarioRepository.findById(idUsuario) } returns Optional.of(usuario)

        val resultado = usuarioService.getUsuarioPorId(idUsuario)

        assertEquals(usuario, resultado)
    }

    @Test
    fun `Deve lançar NotFoundException quando não encontrar o usuário pelo ID`() {

        val idUsuarioNaoExistente = 2L
        every { usuarioRepository.findById(idUsuarioNaoExistente) } returns Optional.empty()

        assertThrows(NotFoundException::class.java) { usuarioService.getUsuarioPorId(idUsuarioNaoExistente) }
    }

    @Test
    fun `Deve retornar o usuário pelo e-mail`() {

        val emailUsuario = "usuario@teste.com"
        every { usuarioRepository.findByEmail(emailUsuario) } returns usuario

        val resultado = usuarioService.getUsuarioPorEmail(emailUsuario)

        assertEquals(usuario, resultado)
    }

    @Test
    fun `Deve retornar null quando não encontrar o usuário pelo e-mail`() {

        val emailUsuarioNaoExistente = "naoexiste@teste.com"
        every { usuarioRepository.findByEmail(emailUsuarioNaoExistente) } returns null

        val resultado = usuarioService.getUsuarioPorEmail(emailUsuarioNaoExistente)

        assertEquals(null, resultado)
    }

    @Test
    fun `Deve atualizar o usuário`() {

        val usuarioAtualizado = usuario.copy(nome = "Novo Nome")
        every { usuarioRepository.findById(usuario.id!!) } returns Optional.of(usuario)
        every { usuarioRepository.save(usuario) } returns usuarioAtualizado

        val resultado = usuarioService.atualizarUsuario(usuarioAtualizado)

        assertEquals(usuarioAtualizado, resultado)
    }

    @Test
    fun `Deve lançar NotFoundException ao tentar atualizar usuário inexistente`() {

        val usuarioInexistente = usuario.copy(id = 2L)
        every { usuarioRepository.findById(usuarioInexistente.id!!) } returns Optional.empty()


        assertThrows(NotFoundException::class.java) { usuarioService.atualizarUsuario(usuarioInexistente) }
    }
}