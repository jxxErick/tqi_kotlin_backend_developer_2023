package com.grocery.jumarket.domain

import com.grocery.jumarket.repositories.CarrinhoRepository
import jakarta.persistence.*

@Entity
data class Usuario(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var email: String,
    var nome: String,
    var cpf: String,

    @OneToMany(mappedBy = "usuario")
    val carrinho: MutableList<Carrinho> = mutableListOf(),

    @OneToMany(mappedBy = "usuario")
    val venda: List<Venda> = mutableListOf(),
)


