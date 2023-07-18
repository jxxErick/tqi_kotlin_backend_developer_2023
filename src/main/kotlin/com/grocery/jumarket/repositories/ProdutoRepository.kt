package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.domain.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProdutoRepository : JpaRepository<Produto, Long>{
@Query("SELECT p FROM Produto p WHERE p.categoria = :categoria")
fun findByCategoria(categoria: Categoria): List<Produto>
}