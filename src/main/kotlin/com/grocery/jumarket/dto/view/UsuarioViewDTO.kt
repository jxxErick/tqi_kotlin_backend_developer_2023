package com.grocery.jumarket.dto.view

import com.grocery.jumarket.domain.Usuario

data class UsuarioViewDTO(
    val id: Long,
    val email: String,
    val nome: String,
    val cpf: String
) {
    constructor(usuario: Usuario) : this(
        id = usuario.id ?: 0L,
        email = usuario.email,
        nome = usuario.nome,
        cpf = usuario.cpf
    )
}