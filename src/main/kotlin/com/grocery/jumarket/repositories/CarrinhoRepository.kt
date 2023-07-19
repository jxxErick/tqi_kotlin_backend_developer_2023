package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Carrinho
import com.grocery.jumarket.domain.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CarrinhoRepository : JpaRepository<Carrinho, Long>   {

}