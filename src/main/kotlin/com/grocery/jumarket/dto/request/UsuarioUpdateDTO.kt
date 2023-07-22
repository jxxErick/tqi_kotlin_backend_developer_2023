package com.grocery.jumarket.dto.request

import com.grocery.jumarket.domain.Usuario
import jakarta.validation.constraints.NotEmpty

data class UsuarioUpdateDTO(
    @field:NotEmpty(message = "O campo não pode ficar em branco") val nome: String,
    @field:NotEmpty(message = "O campo não pode ficar em branco") val cpf: String,
    @field:NotEmpty(message = "O campo não pode ficar em branco") val email: String
) {
    fun toUsuario(usuarioExistente: Usuario): Usuario {
        return Usuario(
            id = usuarioExistente.id,
            email = this.email,
            nome = this.nome,
            cpf = this.cpf,
            carrinho = usuarioExistente.carrinho
        )
    }
}