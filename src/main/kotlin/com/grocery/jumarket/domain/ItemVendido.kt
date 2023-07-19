package com.grocery.jumarket.domain

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
data class ItemVendido(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    val produto: Produto,

    @Column(nullable = false)
    val quantidade: Long,

    @Column(nullable = false)
    val precoUnitario: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id")
    var venda: Venda? = null
)