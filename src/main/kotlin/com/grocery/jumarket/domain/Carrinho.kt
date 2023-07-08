package com.grocery.jumarket.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
data class Carrinho(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "carrinho")
        val itens: List<ItemCarrinho> = mutableListOf(),

        @ManyToOne
        val venda: Venda? = null

        )