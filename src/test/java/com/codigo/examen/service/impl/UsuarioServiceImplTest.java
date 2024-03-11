package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testCreateUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("test");
        usuario.setPassword("1234");

        when(usuarioRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> responseEntity = usuarioService.createUsuario(usuario);

        assertNotNull(responseEntity);
        assertEquals(usuario, responseEntity.getBody());
    }
    @Test
    void testGetUsuarioById() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> responseEntity = usuarioService.getUsuarioById(1L);

        assertNotNull(responseEntity);
        assertEquals(usuario, responseEntity.getBody());
    }

    @Test
    void testUpdateUsuarioExistingUsername() {
        Long id = 1L;
        Usuario existingUsuario = new Usuario();
        existingUsuario.setIdUsuario(id);
        existingUsuario.setUsername("existingUsername");
        existingUsuario.setTelefono("existingTelefono");

        Usuario updatedUsuario = new Usuario();
        updatedUsuario.setIdUsuario(id);
        updatedUsuario.setUsername("newUsername");
        updatedUsuario.setTelefono("newTelefono");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existingUsuario));
        when(usuarioRepository.findByUsername("newUsername")).thenReturn(Optional.of(existingUsuario));

        ResponseEntity<Usuario> responseEntity = usuarioService.updateUsuario(id, updatedUsuario);

        assertEquals(ResponseEntity.badRequest().body(null), responseEntity);
        verify(usuarioRepository, never() ).save(any());
    }





}