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


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun criarCategoria(@RequestBody categoriaDTO: CategoriaDTO): ResponseEntity<CategoriaDTO> {
        val novaCategoria = categoriaService.criarCategoria(categoriaDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria)
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarCategorias(): ResponseEntity<List<CategoriaDTO>> {
        val categorias = categoriaService.listarCategorias()
        return ResponseEntity.ok(categorias)
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCategoriaPorId(@PathVariable id: Long): ResponseEntity<CategoriaDTO> {
        val categoria = categoriaService.getCategoriaPorId(id)
        return ResponseEntity.ok(categoria)
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarCategoria(@PathVariable id: Long) {
        categoriaService.deletarCategoria(id)
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun editarCategoria(@PathVariable id: Long, @RequestBody newCategoriaDTO: NewCategoriaDTO): Categoria {
        return categoriaService.editarCategoria(id, newCategoriaDTO)
    }

}