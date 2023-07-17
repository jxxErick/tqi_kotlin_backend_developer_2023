package com.grocery.jumarket.domain

import jakarta.persistence.*

@Entity
data class Carrinho(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToMany
        @JoinTable(
                name = "produto_carrinho",
                joinColumns = [JoinColumn(name = "carrinho_id")],
                inverseJoinColumns = [JoinColumn(name = "produto_id")]
        )
        var produtos: MutableList<Produto> = mutableListOf(),

        var precoTotal: Double = 0.0,

        @OneToOne(mappedBy = "carrinho")
        var venda: Venda?,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        var usuario: Usuario?


        )