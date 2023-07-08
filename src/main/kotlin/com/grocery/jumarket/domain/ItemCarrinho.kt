package com.grocery.jumarket.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne


data class ItemCarrinho (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        val produto: Produto,

        val quantidade: Int,
        val precoVenda: Double,

        @ManyToOne
        val carrinho: Carrinho

)