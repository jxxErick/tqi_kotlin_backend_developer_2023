package com.grocery.jumarket.controller

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.request.CategoriaDTO
import com.grocery.jumarket.dto.request.NewCategoriaDTO
import com.grocery.jumarket.dto.view.CategoriaViewDTO
import com.grocery.jumarket.service.impl.CategoriaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categorias")
class CategoriaResource(private val categoriaService: CategoriaService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criarCategoria(@RequestBody categoriaDTO: CategoriaDTO): ResponseEntity<CategoriaViewDTO> {
        val categoria = Categoria(nome = categoriaDTO.nome)
        val novaCategoria = categoriaService.criarCategoria(categoria)
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaViewDTO(novaCategoria))
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarCategorias(): ResponseEntity<List<CategoriaViewDTO>> {
        val categorias = categoriaService.listarCategorias()
        val categoriasDTO = categorias.map { CategoriaViewDTO(it) }
        return ResponseEntity.ok(categoriasDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCategoriaPorId(@PathVariable id: Long): ResponseEntity<CategoriaViewDTO> {
        val categoria = categoriaService.buscarCategoriaPorId(id)
        return ResponseEntity.ok(CategoriaViewDTO(categoria))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarCategoria(@PathVariable id: Long) {
        categoriaService.deletarCategoria(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun editarCategoria(
        @PathVariable id: Long,
        @RequestBody categoriaUpdateDTO: CategoriaDTO
    ): ResponseEntity<CategoriaViewDTO> {
        val categoriaExistente = categoriaService.buscarCategoriaPorId(id)
        val categoriaAtualizada = Categoria(nome = categoriaUpdateDTO.nome, id = categoriaExistente.id)
        val categoriaAtualizadaResultado = categoriaService.editarCategoria(id, categoriaAtualizada)
        return ResponseEntity.ok(CategoriaViewDTO(categoriaAtualizadaResultado))
    }
}