package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Produto
import org.springframework.data.jpa.repository.JpaRepository

interface ProdutoRepository : JpaRepository<Produto, Long>
