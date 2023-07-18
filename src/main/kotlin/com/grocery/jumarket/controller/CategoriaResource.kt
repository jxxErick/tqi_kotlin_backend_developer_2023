package com.grocery.jumarket.controller

import com.grocery.jumarket.domain.Categoria
import com.grocery.jumarket.dto.CategoriaDTO
import com.grocery.jumarket.dto.NewCategoriaDTO
import com.grocery.jumarket.service.impl.CategoriaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categorias")
class CategoriaResource(private val categoriaService: CategoriaService) {

    // Cria uma categoria
    @PostMapping
    fun criarCategoria(@RequestBody categoriaDTO: CategoriaDTO): ResponseEntity<CategoriaDTO> {
        val novaCategoria = categoriaService.criarCategoria(categoriaDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria)
    }

    // Lista elas
    @GetMapping
    fun listarCategorias(): ResponseEntity<List<CategoriaDTO>> {
        val categorias = categoriaService.listarCategorias()
        return ResponseEntity.ok(categorias)
    }

    // pega uma pela id
    @GetMapping("/{id}")
    fun getCategoriaPorId(@PathVariable id: Long): ResponseEntity<CategoriaDTO> {
        val categoria = categoriaService.getCategoriaPorId(id)
        return ResponseEntity.ok(categoria)
    }

    // deleta passando id
    @DeleteMapping("/{id}")
    fun deletarCategoria(@PathVariable id: Long) {
        categoriaService.deletarCategoria(id)
    }

    // edita ela passando o id
    @PutMapping("/{id}")
    fun editarCategoria(@PathVariable id: Long, @RequestBody newCategoriaDTO: NewCategoriaDTO): Categoria {
        return categoriaService.editarCategoria(id, newCategoriaDTO)
    }

}