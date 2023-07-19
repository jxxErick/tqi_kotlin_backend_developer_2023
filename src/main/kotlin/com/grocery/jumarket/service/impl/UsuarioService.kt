package com.grocery.jumarket.service.impl
import com.grocery.jumarket.domain.Usuario
import com.grocery.jumarket.dto.NewUsuarioDTO
import com.grocery.jumarket.dto.UsuarioDTO
import com.grocery.jumarket.repositories.CarrinhoRepository
import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.repositories.UsuarioRepository
import com.grocery.jumarket.service.IUsuarioService
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository, private val carrinhoRepository: CarrinhoRepository) : IUsuarioService {
    override fun criarUsuario(usuarioDTO: NewUsuarioDTO): UsuarioDTO {
        val emailExistente = usuarioRepository.findByEmail(usuarioDTO.email)
        if (emailExistente != null) {
            throw BusinessException("E-mail já está em uso")
        }

        val cpfExistente = usuarioRepository.findByCpf(usuarioDTO.cpf)
        if (cpfExistente != null) {
            throw BusinessException("CPF já está cadastrado")
        }

        val novoUsuario = Usuario(
            email = usuarioDTO.email,
            nome = usuarioDTO.nome,
            cpf = usuarioDTO.cpf,
           carrinho = null
        )

        val usuarioSalvo = usuarioRepository.save(novoUsuario)


        return UsuarioDTO(
            id = usuarioSalvo.id,
            email = usuarioSalvo.email,
            nome = usuarioSalvo.nome,
            cpf = usuarioSalvo.cpf
        )
    }


    override fun listarUsuarios(): List<UsuarioDTO> {
        val usuarios = usuarioRepository.findAll()
        return usuarios.map { usuario ->
            UsuarioDTO(
                id = usuario.id,
                email = usuario.email,
                nome = usuario.nome,
                cpf = usuario.cpf
            )
        }
    }

    override fun getUsuarioPorId(id: Long): UsuarioDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        return UsuarioDTO(
            id = usuario.id,
            email = usuario.email,
            nome = usuario.nome,
            cpf = usuario.cpf
        )
    }

    override fun getUsuarioPorEmail(email: String): UsuarioDTO? {
        val usuario = usuarioRepository.findByEmail(email)
        return usuario?.let {
            UsuarioDTO(
                id = it.id,
                email = it.email,
                nome = it.nome,
                cpf = it.cpf
            )
        }
    }

    override fun atualizarUsuario(id: Long, usuarioDTO: NewUsuarioDTO): UsuarioDTO {
        val usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow { NotFoundException("Usuário não encontrado") }

        usuarioExistente.email = usuarioDTO.email
        usuarioExistente.nome = usuarioDTO.nome
        usuarioExistente.cpf = usuarioDTO.cpf

        val usuarioAtualizado = usuarioRepository.save(usuarioExistente)

        return UsuarioDTO(
            id = usuarioAtualizado.id,
            email = usuarioAtualizado.email,
            nome = usuarioAtualizado.nome,
            cpf = usuarioAtualizado.cpf
        )
    }


}