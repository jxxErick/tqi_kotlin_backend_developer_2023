package com.grocery.jumarket.domain

import jakarta.persistence.*

@Entity
data class Produto (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var nome: String,
        var unidadeDeMedida: String,
        var precoUnitario: Double,

        @ManyToOne
        val categoria: Categoria

)
