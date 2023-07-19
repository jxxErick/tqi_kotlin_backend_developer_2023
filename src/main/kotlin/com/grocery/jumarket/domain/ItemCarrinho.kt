package com.grocery.jumarket.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal


@Entity
data class ItemCarrinho(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    var produto: Produto,

    @Column(nullable = false)
    var quantidade: Long,

    @Column(nullable = false)
    var precoUnitario: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id")
    var carrinho: Carrinho? = null
) {
    constructor(produto: Produto, quantidade: Long, precoUnitario: BigDecimal) :
            this(null, produto, quantidade, precoUnitario)
}
