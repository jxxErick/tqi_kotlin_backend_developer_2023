package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Venda
import org.springframework.data.jpa.repository.JpaRepository

interface VendaRepository: JpaRepository<Venda, Long> {
}