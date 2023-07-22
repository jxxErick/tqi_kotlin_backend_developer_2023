package com.grocery.jumarket.dto.request

import com.grocery.jumarket.domain.Usuario
import jakarta.validation.constraints.NotEmpty


data class UsuarioDTO(
    val id: Long,
    @field:NotEmpty(message = "O campo não pode ficar em branco")  val email: String,
    @field:NotEmpty(message = "O campo não pode ficar em branco") val nome: String,
    @field:NotEmpty(message = "O campo não pode ficar em branco") val cpf: String
) {
    fun toUsuario(): Usuario {
        return Usuario(
            email = this.email,
            nome = this.nome,
            cpf = this.cpf,
            carrinho = null,
            id = this.id
        )
    }
}