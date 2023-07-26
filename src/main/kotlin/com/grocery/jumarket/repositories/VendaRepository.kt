package com.grocery.jumarket.repositories

import com.grocery.jumarket.domain.Venda
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface VendaRepository: JpaRepository<Venda, Long> {
    fun findAllByDataVenda(dataVenda: LocalDate): List<Venda>
}