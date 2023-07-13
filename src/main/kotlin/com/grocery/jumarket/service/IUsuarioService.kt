package com.grocery.jumarket.service


import com.grocery.jumarket.dto.NewUsuarioDTO
import com.grocery.jumarket.dto.UsuarioDTO


interface IUsuarioService {
    fun criarUsuario(usuarioDTO: NewUsuarioDTO): UsuarioDTO
    fun listarUsuarios(): List<UsuarioDTO>
    fun getUsuarioPorId(id: Long): UsuarioDTO
    fun getUsuarioPorEmail(email: String): UsuarioDTO?
    fun atualizarUsuario(id: Long, usuarioDTO: NewUsuarioDTO): UsuarioDTO
    fun deletarUsuario(id: Long)
}