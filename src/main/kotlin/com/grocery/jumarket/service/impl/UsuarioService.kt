package com.grocery.jumarket.service.impl
import com.grocery.jumarket.domain.Usuario
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.service.IUsuarioService
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository) : IUsuarioService {
    override fun criarUsuario(usuario: Usuario): Usuario {
        val emailExistente = usuarioRepository.findByEmail(usuario.email)
        if (emailExistente != null) {
            throw BusinessException("E-mail já está em uso")
        }

        val cpfExistente = usuarioRepository.findByCpf(usuario.cpf)
        if (cpfExistente != null) {
            throw BusinessException("CPF já está cadastrado")
        }

        return usuarioRepository.save(usuario)
    }

    override fun listarUsuarios(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    override fun getUsuarioPorId(id: Long): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { NotFoundException("Usuário não encontrado") }
    }

    override fun getUsuarioPorEmail(email: String): Usuario? {
        return usuarioRepository.findByEmail(email)
    }

    override fun atualizarUsuario(usuario: Usuario): Usuario? {
        val usuarioExistente = usuarioRepository.findById(usuario.id)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        usuarioExistente.email = usuario.email
        usuarioExistente.nome = usuario.nome
        usuarioExistente.cpf = usuario.cpf

        return usuarioRepository.save(usuarioExistente)
    }

}