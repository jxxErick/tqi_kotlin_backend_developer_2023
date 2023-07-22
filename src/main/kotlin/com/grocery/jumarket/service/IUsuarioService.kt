package com.grocery.jumarket.service


import com.grocery.jumarket.domain.Usuario


interface IUsuarioService {
    fun criarUsuario(usuario: Usuario): Usuario
    fun listarUsuarios(): List<Usuario>
    fun getUsuarioPorId(id: Long): Usuario
    fun getUsuarioPorEmail(email: String): Usuario?
    fun atualizarUsuario(usuario: Usuario): Usuario?

}