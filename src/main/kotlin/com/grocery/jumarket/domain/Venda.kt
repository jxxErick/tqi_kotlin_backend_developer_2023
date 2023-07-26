package com.grocery.jumarket.domain

import com.grocery.jumarket.dto.request.ItemVendidoDTO
import com.grocery.jumarket.dto.request.UsuarioDTO
import com.grocery.jumarket.dto.view.VendaViewDTO
import com.grocery.jumarket.ennumeration.FormaDePagamento
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

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
        val itensVendidos: MutableList<ItemVendido> = mutableListOf(),

        @Column(nullable = false)
        var dataVenda: LocalDate = LocalDate.now()
) {
        fun adicionarItemVendido(itemVendido: ItemVendido) {
                itensVendidos.add(itemVendido)
                itemVendido.venda = this
        }
        fun toDTO(): VendaViewDTO {
                val usuarioDTO = UsuarioDTO(
                        id = usuario.id,
                        email = usuario.email,
                        nome = usuario.nome,
                        cpf = usuario.cpf
                )

                val itensVendidosDTO = itensVendidos.map { itemVendido ->
                        ItemVendidoDTO(
                                produtoId = itemVendido.produto.id!!,
                                quantidade = itemVendido.quantidade,
                                precoUnitario = itemVendido.precoUnitario
                        )
                }

                return VendaViewDTO(
                        id = id!!,
                        usuario = usuarioDTO,
                        valorTotal = valorTotal,
                        formaDePagamento = formaDePagamento.name,
                        itensVendidos = itensVendidosDTO
                )
        }
}