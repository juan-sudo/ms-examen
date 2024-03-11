package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.service.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Key;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceImplTest {
    private JWTService jwtService;

    @Mock
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void generateToken() {
        jwtService = new JWTServiceImpl();
        String token = jwtService.generateToken(usuario);


        when(usuario.getUsername()).thenReturn("testUser");

        assertNotNull(token);
        verify(usuario, times(1)).getUsername();
        verify(usuario, times(1)).getRoles();
    }





}