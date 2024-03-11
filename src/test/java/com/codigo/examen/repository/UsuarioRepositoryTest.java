package com.codigo.examen.repository;

import com.codigo.examen.entity.Usuario;
import com.codigo.examen.service.UsuarioService;
import com.codigo.examen.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioRepositoryTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testFindByUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("juan3");
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));

        Optional<Usuario> optionalUsuario = usuarioService.findByUsername("juan3");

        assertTrue(optionalUsuario.isPresent());
        //assertTrue(optionalUsuario.get().getUsername().equals("juan3"));
    }

}