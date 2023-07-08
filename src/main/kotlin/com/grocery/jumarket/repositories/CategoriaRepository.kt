package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Categoria
import org.springframework.data.jpa.repository.JpaRepository

interface CategoriaRepository : JpaRepository<Categoria, Long> {

}