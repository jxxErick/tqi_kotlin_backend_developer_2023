package com.grocery.jumarket.domain

import jakarta.persistence.*

@Entity
data class Categoria (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false) var nome: String,

    @OneToMany(mappedBy = "categoria")
    val produtos: List<Produto> = mutableListOf()

)