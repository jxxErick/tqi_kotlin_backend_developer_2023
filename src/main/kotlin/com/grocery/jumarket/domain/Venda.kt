package com.grocery.jumarket.domain

import com.grocery.jumarket.ennumeration.formaDePagamento
import jakarta.persistence.*

@Entity
data class Venda (

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        val valorTotal: Double,
        val formaDePagamento: formaDePagamento,

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "carrinho_id")
        val carrinho: Carrinho,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        val usuario: Usuario?
)