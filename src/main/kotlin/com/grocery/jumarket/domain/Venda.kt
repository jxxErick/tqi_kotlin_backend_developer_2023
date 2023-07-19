package com.grocery.jumarket.domain

import com.grocery.jumarket.ennumeration.FormaDePagamento
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
data class Venda(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        val usuario: Usuario,

        @Column(nullable = false)
        var valorTotal: BigDecimal,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val formaDePagamento: FormaDePagamento,

        @OneToMany(mappedBy = "venda", cascade = [CascadeType.ALL], orphanRemoval = true)
        val itensVendidos: MutableList<ItemVendido> = mutableListOf()
) {
        fun adicionarItemVendido(itemVendido: ItemVendido) {
                itensVendidos.add(itemVendido)
                itemVendido.venda = this
        }


}