package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByEmail(email: String): Usuario?
    fun findByCpf(cpf: String): Usuario?
}