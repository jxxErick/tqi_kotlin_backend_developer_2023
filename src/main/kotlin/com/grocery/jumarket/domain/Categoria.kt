package com.grocery.jumarket.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Cascade

@Entity
data class Categoria (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @Column(nullable = false) @NotNull(message = "O nome é obrigatório")var nome: String,

    @OneToMany(mappedBy = "categoria", cascade = [CascadeType.REMOVE]) val produtos: List<Produto> = mutableListOf()

)