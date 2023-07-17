package com.grocery.jumarket.domain

import com.grocery.jumarket.ennumeration.StatusCarrinho
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
        var usuario: Usuario?,
        @Enumerated(EnumType.STRING)
        var status: StatusCarrinho = StatusCarrinho.PENDENTE


        ) {
        fun adicionarProduto(produto: Produto) {
                produtos.add(produto)
                precoTotal += produto.precoUnitario
        }

        fun removerProdutoPorId(produtoId: Long) {
                val produto = produtos.find { it.id == produtoId }
                if (produto != null) {
                        produtos.remove(produto)
                        precoTotal -= produto.precoUnitario
                }
        }
}
