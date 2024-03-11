package com.codigo.examen.controller;

import com.codigo.examen.entity.Usuario;
import com.codigo.examen.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ms-examen/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UsuarioService usuarioService;
    @GetMapping("/hola")
    public ResponseEntity<String> saludoAdmin(){
        return ResponseEntity.ok("Hola Usuario");
    }

    @GetMapping("/listarTodo/{id}")
    public ResponseEntity<Usuario> getAllUsers(@PathVariable Long id) {
        ResponseEntity<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario;
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable Long id, @RequestBody Usuario usuario){

        return usuarioService.updateUsuario(id,usuario);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Usuario> deleteUser(@PathVariable Long id){
        return usuarioService.deleteUsuario(id);
    }


}
