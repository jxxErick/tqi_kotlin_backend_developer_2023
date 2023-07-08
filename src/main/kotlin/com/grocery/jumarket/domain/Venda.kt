package com.grocery.jumarket.domain

import com.grocery.jumarket.ennumeration.formaDePagamento
import jakarta.persistence.*

@Entity
data class Venda (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        val valorTotal: Double,
        val formaDePagamento: formaDePagamento,

        @OneToMany(mappedBy = "venda", cascade = [CascadeType.ALL])
        val carrinhos: List<Carrinho> = mutableListOf()

)