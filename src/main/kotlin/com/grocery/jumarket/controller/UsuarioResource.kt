package com.grocery.jumarket.controller

import com.grocery.jumarket.dto.request.UsuarioDTO
import com.grocery.jumarket.dto.request.UsuarioUpdateDTO
import com.grocery.jumarket.dto.view.UsuarioViewDTO
import com.grocery.jumarket.service.IUsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")
class UsuarioResource(private val usuarioService: IUsuarioService) {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun salvarUsuario(@RequestBody usuarioDTO: UsuarioDTO): ResponseEntity<UsuarioViewDTO> {
        val usuario = usuarioDTO.toUsuario()
        val novoUsuario = usuarioService.criarUsuario(usuario)

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioViewDTO(novoUsuario))
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun listarUsuarios(): ResponseEntity<List<UsuarioViewDTO>> {
        val usuarios = usuarioService.listarUsuarios()
        val usuariosDTO = usuarios.map { UsuarioViewDTO(it) }
        return ResponseEntity.ok(usuariosDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun buscarUsuarioPorId(@PathVariable id: Long): ResponseEntity<UsuarioViewDTO> {
        val usuario = usuarioService.getUsuarioPorId(id)
        return ResponseEntity.ok(UsuarioViewDTO(usuario))
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun buscarUsuarioPorEmail(@PathVariable email: String): ResponseEntity<UsuarioViewDTO?> {
        val usuario = usuarioService.getUsuarioPorEmail(email)
        return ResponseEntity.ok(usuario?.let { UsuarioViewDTO(it) })
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun atualizarUsuario(@PathVariable id: Long, @RequestBody usuarioUpdateDTO: UsuarioUpdateDTO): ResponseEntity<UsuarioViewDTO> {
        val usuarioExistente = usuarioService.getUsuarioPorId(id)
        val usuarioAtualizado = usuarioUpdateDTO.toUsuario(usuarioExistente)
        val usuarioAtualizadoResultado = usuarioService.atualizarUsuario(usuarioAtualizado)
        return ResponseEntity.ok(usuarioAtualizadoResultado?.let { UsuarioViewDTO(it) })
    }
}