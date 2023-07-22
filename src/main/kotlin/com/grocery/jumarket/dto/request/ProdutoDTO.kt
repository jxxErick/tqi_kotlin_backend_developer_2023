package com.grocery.jumarket.dto.request

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.domain.Produto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ProdutoDTO(
        @field:NotBlank(message = "O nome não pode estar vazio") val nome: String,
        @field:NotBlank(message = "A unidade de medida não pode estar vazia") val unidadeDeMedida: String,
        @field:NotNull(message = "O preço não pode estar vazio") val precoUnitario: BigDecimal,
        @field:NotNull(message = "A categoria não pode estar vazia") val categoriaId: Long,
        @field:NotNull(message = "A quantidade em estoque não pode estar vazia") val quantidadeEstoque: Long
){
        fun toProduto(): Produto {
                return Produto(
                        nome = this.nome,
                        unidadeDeMedida = this.unidadeDeMedida,
                        precoUnitario = this.precoUnitario,
                        quantidadeEstoque = this.quantidadeEstoque,
                        categoria = Categoria(nome = "", id = this.categoriaId)
                )
        }
}