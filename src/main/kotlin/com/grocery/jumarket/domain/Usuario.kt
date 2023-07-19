package com.grocery.jumarket.domain

import com.grocery.jumarket.repositories.CarrinhoRepository
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

@Entity
data class Usuario(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(nullable = false) @NotNull(message = "O email nao pode estar vazio") @Email var email: String,
    @Column(nullable = false) @NotNull(message = "O nome n pode estar vazio")var nome: String,
    @Column(nullable = false) @NotNull(message = "O cpf nao pode estar vazio")var cpf: String,
    @OneToOne(mappedBy = "usuario")  @JoinColumn(name = "carrinho_id") var carrinho: Carrinho?,
    @OneToMany(mappedBy = "usuario") val venda: List<Venda> = mutableListOf()
)


