package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Carrinho
import org.springframework.data.jpa.repository.JpaRepository

interface CarrinhoRepository : JpaRepository<Carrinho, Long>  {
}