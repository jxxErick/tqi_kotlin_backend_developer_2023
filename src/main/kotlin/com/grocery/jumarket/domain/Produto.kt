package com.grocery.jumarket.domain

import jakarta.persistence.ManyToOne

data class Produto (

        var nome: String,
        var unidadeDeMedida: String,
        var precoUnitario: Double,

        @ManyToOne
        val categoria: Categoria

)
