package com.codigo.examen.service;

import com.codigo.examen.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UsuarioService {
    ResponseEntity<Usuario> createUsuario(Usuario usuario);
    ResponseEntity<Usuario> getUsuarioById(Long id);
    ResponseEntity<Usuario> updateUsuario(Long id, Usuario usuario);
    ResponseEntity<Usuario> deleteUsuario(Long id);

    Optional<Usuario> findByUsername(String username);




}
