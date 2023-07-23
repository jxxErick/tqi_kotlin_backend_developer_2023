package com.grocery.jumarket.domain


import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@Entity
data class Carrinho(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        var usuario: Usuario,

        @OneToMany(mappedBy = "carrinho", cascade = [CascadeType.ALL], orphanRemoval = true)
        var itens: MutableList<ItemCarrinho> = mutableListOf(),

        @Column(nullable = false)
        @NotNull(message = "A quantidade de itens não pode estar vazia")
        var quantidadeItens: Long = 0,

        @Column(nullable = false)
        @NotNull(message = "O valor total não pode estar vazio")
        var valorTotal: BigDecimal = BigDecimal.ZERO
) {
        fun adicionarItem(item: ItemCarrinho) {
                itens.add(item)
                item.carrinho = this
                quantidadeItens += item.quantidade
                valorTotal += item.precoUnitario.multiply(BigDecimal.valueOf(item.quantidade))
        }
        fun removerItem(itemId: Long) {
                val item = itens.find { it.id == itemId }
                if (item != null) {
                        itens.remove(item)
                        item.carrinho = null
                        quantidadeItens -= item.quantidade
                        valorTotal -= item.precoUnitario.multiply(BigDecimal.valueOf(item.quantidade))
                }
        }
        override fun hashCode(): Int {
                return id?.hashCode() ?: 0
        }
}
