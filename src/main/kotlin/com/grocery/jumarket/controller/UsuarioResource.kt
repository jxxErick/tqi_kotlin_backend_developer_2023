package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.NewUsuarioDTO
import com.grocery.jumarket.dto.UsuarioDTO
import com.grocery.jumarket.service.IUsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")
class UsuarioResource(private val usuarioService: IUsuarioService) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun criarUsuario(@RequestBody usuarioDTO: NewUsuarioDTO): ResponseEntity<UsuarioDTO> {
        val novoUsuario = usuarioService.criarUsuario(usuarioDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarUsuarios(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = usuarioService.listarUsuarios()
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUsuarioPorId(@PathVariable id: Long): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioService.getUsuarioPorId(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getUsuarioPorEmail(@PathVariable email: String): ResponseEntity<UsuarioDTO?> {
        val usuario = usuarioService.getUsuarioPorEmail(email)
        return ResponseEntity.ok(usuario)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun atualizarUsuario(@PathVariable id: Long, @RequestBody usuarioDTO: NewUsuarioDTO): ResponseEntity<UsuarioDTO> {
        val usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO)
        return ResponseEntity.ok(usuarioAtualizado)
    }


}