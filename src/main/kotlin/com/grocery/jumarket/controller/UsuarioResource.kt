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
    //cria um usuario
    @PostMapping
    fun criarUsuario(@RequestBody usuarioDTO: NewUsuarioDTO): ResponseEntity<UsuarioDTO> {
        val novoUsuario = usuarioService.criarUsuario(usuarioDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario)
    }
    //lista os usuarios
    @GetMapping
    fun listarUsuarios(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = usuarioService.listarUsuarios()
        return ResponseEntity.ok(usuarios)
    }
    // pega usuario pela id
    @GetMapping("/{id}")
    fun getUsuarioPorId(@PathVariable id: Long): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioService.getUsuarioPorId(id)
        return ResponseEntity.ok(usuario)
    }
// pega usuario pelo email
    @GetMapping("/email/{email}")
    fun getUsuarioPorEmail(@PathVariable email: String): ResponseEntity<UsuarioDTO?> {
        val usuario = usuarioService.getUsuarioPorEmail(email)
        return ResponseEntity.ok(usuario)
    }
// atualiza alguns campos do usuario pela id
    @PutMapping("/{id}")
    fun atualizarUsuario(
        @PathVariable id: Long,
        @RequestBody usuarioDTO: NewUsuarioDTO
    ): ResponseEntity<UsuarioDTO> {
        val usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO)
        return ResponseEntity.ok(usuarioAtualizado)
    }
// deleta usuario pela id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarUsuario(@PathVariable id: Long) {
        usuarioService.deletarUsuario(id)
    }
}