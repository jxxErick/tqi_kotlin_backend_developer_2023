package com.grocery.jumarket.domain


import io.micrometer.common.lang.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
data class Produto(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
        @Column(nullable = false) @NotNull(message = "O nome nao pode estar vazio")var nome: String,
        @Column(nullable = false) @NotNull(message = "a unidade de medida nao pode estar vazia")var unidadeDeMedida: String,
        @Column(nullable = false) @NotNull(message = "O preco nao pode estar vazio")var precoUnitario: Double,
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "categoria_id") val categoria: Categoria,
        @ManyToMany(mappedBy = "produtos") val carrinhos: MutableList<Carrinho> = mutableListOf()
)
